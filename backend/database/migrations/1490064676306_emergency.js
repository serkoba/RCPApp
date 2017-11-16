'use strict'

const Schema = use('Schema')

class EmergencyTableSchema extends Schema {

  up () {
    this.create('emergency', table => {
      table.increments()
      table.string('email', 254).notNullable().unique()
      table.float('latitude', 12, 2).notNullable()
      table.float('longitude', 12, 2).notNullable()
      table.string('address',254)
      table.integer('emergencyType').notNullable()
      table.timestamps()
    })
  }

  down () {
    this.drop('emergency')
  }

}

module.exports = EmergencyTableSchema
