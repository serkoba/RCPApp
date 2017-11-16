'use strict'

const Schema = use('Schema')

class EmergencyTableSchema extends Schema {

  up () {
    this.table('emergencies', (table) => {
      table.string('firstName',64)
      table.string('lastName',64)
    })
  }

  down () {
    this.table('emergencies', (table) => {
      table.dropColumns('firstName', 'lastName')
    })
  }

}

module.exports = EmergencyTableSchema
