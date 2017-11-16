'use strict'

const Schema = use('Schema')

class UsersTableSchema extends Schema {

  up () {
    this.table('users', (table) => {
      table.float('latitude', 12, 2)
      table.float('longitude', 12, 2)
    })
  }

  down () {
    this.table('users', (table) => {
      table.dropColumns('latitude', 'longitude')
    })
  }

}

module.exports = UsersTableSchema
