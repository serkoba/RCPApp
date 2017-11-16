'use strict'

const UserHooks = exports = module.exports = {}

const User = use('App/Model/User')
const Hash = use('Hash')
const Validator = use('Validator')

UserHooks.validateCreate = function * (next) {
  const createRules = {
    email: 'required|email|unique:users',
    password: 'required|confirmed'
  }
  const validation = yield Validator.validate(this, createRules)
  if (validation.fails()) {
    throw new Error(JSON.stringify(validation.messages()))
  }
  delete this.attributes.password_confirmation
  yield next
}

UserHooks.hashPassword = function * (next) {
  this.password = yield Hash.make(this.password)
  yield next
}

UserHooks.validateUpdate = function * (next) {
  const updateRules = {
    latitude: 'above:-90|under:90',
    longitude: 'above:-180|under:180'
  }
  const validation = yield Validator.validate(this, updateRules)
// @review -- Is there a better way to do this?
  // Make token null
  this.attributes.fcmToken = this.attributes.fcmToken || null
  // Check validations.
  if (validation.fails()) {
    throw new Error(JSON.stringify(validation.messages()))
  }
  delete this.attributes.password_confirmation
  yield next
}