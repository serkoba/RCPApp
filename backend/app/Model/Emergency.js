'use strict'

const Lucid = use('Lucid')
// const EmergencyHooks = use('App/Model/Hooks/Emergency')


class Emergency extends Lucid {
    static boot () {
        super.boot()
    }
}

module.exports = Emergency
