'use strict'

const Schema = use('Schema')

class UsersTableSchema extends Schema {

  up () {
    this.table('users', (table) => {
      // alter users table
      table.string('fcmToken', 200).nullable();
    })
  }

  down () {
    this.table('users', (table) => {
      // opposite of up goes here
      table.dropColumn('fcmToken')
    })
  }

}

module.exports = UsersTableSchema
