'use strict'

const Schema = use('Schema')

class UsersTableSchema extends Schema {

  up () {
    this.table('users', (table) => {
      // alter users table
        table.integer('falseAlarm')
        table.integer('bannedUntil')
        table.integer('lastRCP')
    })
  }

  down () {
    this.table('users', (table) => {
      // opposite of up goes here
      table.dropColumns('falseAlarm', 'bannedUntil','lastRCP')
    })
  }

}

module.exports = UsersTableSchema
