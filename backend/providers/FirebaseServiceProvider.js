const ServiceProvider = require('adonis-fold').ServiceProvider
const FirebaseService = require('./FirebaseService')

class FirebaseProvider extends ServiceProvider {
  * register () {
    // Register our ServiceProvider on the namespace: Adonis/Services/Firebase
    // Obtain application reference: app
    this.app.bind('Adonis/Services/Firebase', (app) => {
      // Obtain application configuration in config/
      const Config = app.use('Adonis/Src/Config')

      // Export our service
      return new FirebaseService(Config)
    })
  }
}

module.exports = FirebaseProvider