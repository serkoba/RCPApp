'use strict'

/*
|--------------------------------------------------------------------------
| Router
|--------------------------------------------------------------------------
|
| AdonisJs Router helps you in defining urls and their actions. It supports
| all major HTTP conventions to keep your routes file descriptive and
| clean.
|
| @example
| Route.get('/user', 'UserController.index')
| Route.post('/user', 'UserController.store')
| Route.resource('user', 'UserController')
*/

const Route = use('Route')

Route.get('/', function * (request, response) {
  yield response.status(503).send('')
})

Route.resource('user', 'UserController')

/**
 * Sign up/in/off
 */
Route.post('/user/new', 'UserController.create')
Route.post('login', 'UserController.login')
Route.post('logout', 'UserController.logout').middleware('auth')

/**
 * Profile
 */
Route.get('profile', 'UserController.profile').middleware('auth')
Route.patch('profile', 'UserController.update').middleware('auth')

/**
 * Remove
 */
Route.get('/user', 'UserController.index')
Route.post('/user', 'UserController.store')

/**
 * Get/abort Help
 */
Route.post('/help', 'EmergencyController.help').middleware('auth')
Route.patch('/help/status', 'EmergencyController.status_update').middleware('auth')

/**
 * Get all emergencies
 */
Route.get('/emergencies', 'EmergencyController.emergencies').middleware('auth')