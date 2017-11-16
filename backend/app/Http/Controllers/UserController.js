'use strict'

const User = use('App/Model/User')
const GeoLocation = use('geolib')
const Database = use('Database')

class UserController {
  * create (request, response) {
    const email = request.input('email')
    const password = request.input('password')
    const passwordConfirm = request.input('password_confirmation')
    const firstName = request.input('firstName')
    const lastName = request.input('lastName')
    const phone = request.input('phone')
    const address = request.input('address')
    const isVolunteer = request.input('isVolunteer')

    try {
      yield User.create({ email: email, password: password, password_confirmation: passwordConfirm, firstName: firstName, lastName: lastName, phone: phone, address: address, isVolunteer: isVolunteer});
      response.status(201).json( { message: "OK" } );
    } catch(e) {
      response.status(400).json( { message: e.message } );
    }
  }

  * login (request, response) {
    const basicAuth = request.auth.authenticator('basic')
    const apiAuth = request.auth.authenticator('api')
    const email = request.input('email')
    const password = request.input('password')

    try {
      yield basicAuth.validate(email, password)
      const user = yield User.findBy( 'email', email)
      const token = yield apiAuth.generate(user)
      response.send({ message: 'OK', token: token.token })
    } catch (e) {
      response.unauthorized(e.message)
    }
  }

   * profile (request, response) {
    const user = yield request.auth.getUser()
    if (user) {
      const raw_myEmergency= yield Database.table('emergencies').where('email',user.email)
      var response_object = user
      if (raw_myEmergency.length > 0){
        var myEmergency = raw_myEmergency[0]
        Object.assign(myEmergency, {usersNotified:myEmergency.notified.split(',').length})
        delete myEmergency.notified
        var emergency_createTime = new Date(myEmergency.created_at + '+0')
        Object.assign(myEmergency, {dateTime:emergency_createTime.getTime()})
        delete myEmergency.notified

        Object.assign(user,{emergency:myEmergency})
      }
      response.ok(response_object)
      return
    }
    response.status(401).json({ message: 'Invalid token' })
  }

  * update (request, response) {
    const user = yield request.auth.getUser()
    const userLocation = request.only( 'location' )
    const userData = request.only('fcmToken')
    const isVolunteer = request.only('isVolunteer')
    const firstName = request.only('firstName')
    const lastName = request.only('lastName')
    const phone = request.only('phone')
    const address = request.only('address')
    const radio = request.only('radio')
    const avail_start = request.only('avail_start')
    const avail_end = request.only('avail_end')
    const falseAlarm = request.only('falseAlarm')
    const bannedUntil = request.only('bannedUntil')
    const lastRCP = request.only('lastRCP')

    user.fill( userLocation.location )
    user.fill( userData )
    user.fill( firstName )
    user.fill( lastName )
    user.fill( phone )
    user.fill( address )
    user.fill( isVolunteer )
    user.fill( radio )
    user.fill( avail_start )
    user.fill( avail_end )
    user.fill( falseAlarm )
    user.fill( bannedUntil )
    user.fill( lastRCP )

    try {
      yield user.save()
      response.status(200).json(user);
    } catch (e) {
      response.status(400).json({ message: e.message })
    }
  }

  * index (request, response) {
    const users = yield User.all()
    response.ok( { users: users.toJSON() } )
  }

  * logout (request, response) {
    const user = yield request.auth.getUser()
    const token = request.header('Authorization').split(' ')[1]
    if (user) {
      user.fcmToken = null
      yield user.save()
      yield request.auth.revoke(user, [token]);
      response.ok({ message: 'Logged out succesfully' })
      return
    }
    response.status(401).json({ message: 'Invalid token' })
  }
}

module.exports = UserController