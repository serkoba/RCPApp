'use strict'

const Schema = use('Schema')

class UsersTableSchema extends Schema {

  up () {
    this.table('users', (table) => {
      // alter Users table
      table.string('firstName',64)
      table.string('lastName',64)
      table.string('phone',15)
      table.string('address',254)
      table.integer('isVolunteer')
    })
  }

  down () {
    this.table('users', (table) => {
      table.dropColumns('firstName', 'lastName','phone','address','isVolunteer')
    })
  }

}

module.exports = UsersTableSchema
