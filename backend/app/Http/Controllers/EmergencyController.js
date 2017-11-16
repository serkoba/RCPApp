'use strict'

const User = use('App/Model/User')
const Emergency = use('App/Model/Emergency')
const GeoLocation = use('geolib')
const Firebase = use('Adonis/Services/Firebase')
const Database = use('Database')
const STATUS_CANCEL = 0, STATUS_ACTIVE = 1, STATUS_TAKEN = 2, STATUS_COMPLETE = 3, STATUS_EXPIRED = 4, STATUS_FALSEALARM = 5
const EmergencyTypes = {1:'Otra',2:'Desmayo',4:'Ataque del corazón',8:'Herida',16:'Pánico'}


class EmergencyController {
    * help (request, response) {
    const user = yield request.auth.getUser()
    const userLocation = request.only('location')
    const emergencyType = request.input('emergencyType')
    const emergencyAddress = request.input('address')
    const emergencyTime = Math.floor(+new Date()/1000)
    const allUsers = yield User.all()
    const FirebaseAuth = Firebase.auth()
    const FirebaseDatabase = Firebase.database()
    const messageOptions = {priority: "high",timeToLive: 3600}
    const createEmergencyFail = 'Emergency could not be saved'
    const sendEmergencyFail = 'Emergency created but could not be sent to users'
    var responseMessage = 'Ok'
    
    var helpMessage = {
      'notification': {
        'title':'Solicitud de ayuda!',
        'body':'Tipo de emergencia: '+ EmergencyTypes[emergencyType] + '',
        'click_action' : '.MainActivity'
      },
      'data': {
          'latitude' : userLocation.location.latitude + '',
          'longitude' : userLocation.location.longitude + '',
          'firstName' : user.firstName + '',
          'lastName' : user.lastName + '',
          'email' : user.email + '',
          'emergency' : emergencyType + '',
          'address' : emergencyAddress + '',
          'datetime' : emergencyTime + ''
      }
    }

    var nearbyUsers = []
    var distance = Number.MAX_VALUE
    var currentTime = 0
    if (user.bannedUntil < new Date()){

      allUsers.forEach(x => {
        if (x.email != user.email)
        {
          distance = GeoLocation.getDistance({latitude: parseFloat(userLocation.location.latitude), longitude: parseFloat(userLocation.location.longitude)},{latitude: parseFloat(x.latitude), longitude: parseFloat(x.longitude)})
          currentTime = new Date().getHours()*100 + new Date().getMinutes()
          if ((x.fcmToken != null) && (distance < x.radio) && ((x.isVolunteer & emergencyType) >= 1) && (parseInt(x.avail_start) < currentTime) && (parseInt(x.avail_end) > currentTime))
              nearbyUsers.push(x.fcmToken)
        }
      });

      try {
        yield Emergency.create({ email: user.email, firstName: user.firstName, lastName: user.lastName, latitude: userLocation.location.latitude, longitude: userLocation.location.longitude, address: emergencyAddress, emergencyType: emergencyType, notified:JSON.stringify(nearbyUsers), status: 1});
    } catch(e) {
        responseMessage = createEmergencyFail
      }
      if (nearbyUsers.length > 0){
        Firebase.messaging().sendToDevice(nearbyUsers,helpMessage,messageOptions)
        .then(function(resp){
          response.status(200).json({ message: responseMessage, count:nearbyUsers.length })
        })
        .catch(function(error){
          response.status(400).json({ message: 'Failed to deliver emergency', count:nearbyUsers.length })
        })
      }
      else response.status(200).json({ message: 'There are no volunteers nearby at this moment', count:0 })
    }
    else {
      var bannedUntil = new Date(user.bannedUntil)
      response.status(400).json({ message: 'Tu cuenta ha sido bloqueada por falsas alarmas hasta el día ' + (bannedUntil.getMonth() + 1) + '/' + bannedUntil.getDate() + '/' +  bannedUntil.getFullYear(), count:0 })
    }
  }

  * emergencies (request, response) {
      const user = yield request.auth.getUser()
      const allEmergencies = yield Emergency.all()
      var attendableEmergencies = []
      var deleteEmergencies = []
      var current = new Date()
      current = new Date(current + '+0')
      var created_at = 0
      allEmergencies.forEach(x => {
        created_at = new Date(x.created_at + ' +0')
        if (((x.attributes.emergencyType & user.isVolunteer) > 0) && (x.email != user.email)){
             if ((x.status == STATUS_EXPIRED) || ((current.getTime() - created_at.getTime()) >= 3600000)){
              deleteEmergencies.push(x.email)
            }
            else {
              attendableEmergencies.push(x.attributes)
            }
        }
    });
      for (var i = 0; i < deleteEmergencies.length; i++){
          yield Database.table('emergencies').where('email',deleteEmergencies[i]).delete()
      }
      response.status(200).json( { count:attendableEmergencies.length, emergencies: attendableEmergencies } )
  }

* status_update (request, response) {
      const user = yield request.auth.getUser()
      const status = request.input('status')
      const email = request.input('email')
      const emergency = yield Emergency.findBy('email',email)
      const user_emergency = yield User.findBy('email',email)
      const messageOptions = {priority: "high",timeToLive: 3600}
      var statusMessage = {
        'notification': {
          'title':'',
          'body':'La solicitud de ' + user_emergency.firstName + ' ' + user_emergency.lastName + ' ha sido ',
          'click_action' : '.MainActivity'
        },
        'data': {
          'email' : user_emergency.email + '',
        }
      }
      var notified_users = JSON.parse(emergency.notified)
      if ((status == STATUS_CANCEL) || (status == STATUS_COMPLETE) || (status == STATUS_FALSEALARM))
      {
        yield Database.table('emergencies').where('email',email).delete()
        if (status == STATUS_CANCEL){
          statusMessage.notification.title = 'Solicitud de ayuda cancelada!'
          statusMessage.notification.body = statusMessage.notification.body + 'cancelada'
        }
        else if (status == STATUS_COMPLETE){
          notified_users.push(user_emergency.fcmToken)
          statusMessage.notification.title = 'Solicitud de ayuda completada!'
          statusMessage.notification.body = statusMessage.notification.body + 'completada'
        }
        else {
          notified_users.push(user_emergency.fcmToken)
          statusMessage.notification.title = 'Solicitud de ayuda cancelada!'
          statusMessage.notification.body = statusMessage.notification.body + 'una falsa alarma'
          user_emergency.falseAlarm += 1
          if (user_emergency.falseAlarm == 3){
            user_emergency.falseAlarm = 0
            var d = new Date()
            d.setDate(d.getDate()+7)
            user_emergency.bannedUntil = d.getTime()
          }
          try {
            yield user_emergency.save()
          } catch (e) {
              console.log('Could not register false alarm to user')
            }
        }
        Firebase.messaging().sendToDevice(notified_users,statusMessage,messageOptions)
          .then(function(resp){
            response.status(200).json({ message: 'Ok', count:notified_users.length })
          })
          .catch(function(error){
            response.status(400).json({ message: 'Failed to notify emergency status update', count:notified_users.length })
          })
      }
      else {
        emergency.status = status
        try {
          yield emergency.save()
          response.status(200).json({message: 'OK'});
        } catch (e) {
          response.status(400).json({ message: 'Status could not be updated'})
        }
    }
  }
}
module.exports = EmergencyController