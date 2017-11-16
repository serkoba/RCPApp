'use strict'

const Schema = use('Schema')

class UsersTableSchema extends Schema {

  up () {
    this.table('users', (table) => {
      // alter users table
      table.string('avail_start')
      table.string('avail_end')
    })
  }

  down () {
    this.table('users', (table) => {
      // opposite of up goes here
      table.dropColumns('avail_start', 'avail_end')
    })
  }

}

module.exports = UsersTableSchema
