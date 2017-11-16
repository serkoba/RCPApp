'use strict'

const Schema = use('Schema')

class EmergencyTableSchema extends Schema {

  up () {
    this.table('emergency', (table) => {
      // alter emergency table
      table.integer('status')
    })
  }

  down () {
    this.table('emergency', (table) => {
      // opposite of up goes here
      table.dropColumns('status')
    })
  }

}

module.exports = EmergencyTableSchema
