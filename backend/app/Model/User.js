'use strict'

const Lucid = use('Lucid')
const UserHooks = use('App/Model/Hooks/User')

class User extends Lucid {

  static boot () {
    super.boot()
    // Add hooks
    this.addHook('beforeCreate', 'validateCreation', UserHooks.validateCreate)
    this.addHook('beforeCreate', 'passwordEncryption', UserHooks.hashPassword)
    this.addHook('beforeUpdate', 'validateUpdate', UserHooks.validateUpdate)
  }

  static get hidden () {
    return ['password']
  }

  apiTokens () {
    return this.hasMany('App/Model/Token')
  }

}

module.exports = User
