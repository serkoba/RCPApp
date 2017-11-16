'use strict'

const Schema = use('Schema')

class EmergencyTableSchema extends Schema {

  up () {
    this.rename('emergency','emergencies')
  }

  down () {
    this.rename('emergencies','emergency')
  }

}

module.exports = EmergencyTableSchema
