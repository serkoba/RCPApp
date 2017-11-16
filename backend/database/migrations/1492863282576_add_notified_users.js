'use strict'

const Schema = use('Schema')

class EmergencyTableSchema extends Schema {

  up () {
    this.table('emergency', (table) => {
      // alter emergency table
      table.text('notified')
    })
  }

  down () {
    this.table('emergency', (table) => {
      // opposite of up goes here
      table.dropColumns('notified')
    })
  }

}

module.exports = EmergencyTableSchema
