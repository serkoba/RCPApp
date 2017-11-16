class FirebaseService {
  constructor (Config) {
    // Include our firebase-admin module
    // Find out more at: https://firebase.google.com/docs/admin/setup
    const FirebaseAdmin = require('firebase-admin')
    // Obtain Firebase configuration from config/services.js
    // Create the file if it doesn't exist and Adonis will pick it up
    const FirebaseConfig = Config.get('services.firebase') 

    // Initialize our priviledged firebase admin application
    // FirebaseAdmin.initializeApp({
    //   credential: FirebaseAdmin.credential.cert({
    //     projectId: FirebaseConfig.credentials.projectId,
    //     clientEmail: FirebaseConfig.credentials.clientEmail,
    //     privateKey: FirebaseConfig.credentials.privateKey
    //   }),

    FirebaseAdmin.initializeApp({
      credential: FirebaseAdmin.credential.cert({
        projectId: "red-ciudadana-f31ed",
        clientEmail: "firebase-adminsdk-z8fg8@red-ciudadana-f31ed.iam.gserviceaccount.com",
        privateKey: "-----BEGIN PRIVATE KEY-----\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCyKnnJs5sPy7N3\natJwcWv1Gwi/zwRxGqPIG6wlEc22M0xsplEx2IqsXPJBNc+cg/TGqxTydaI0F4fS\n1900evrJMatS71HnREqFGv12y70XDTnlJudHVZ1UJvDZuVYdA4w0g2QpzHFV+2d9\nU8Ts8Axj7JSFPWJF1eALhChoA5eMWbM5/O5QYqMgXemfqrRXjxCHFAzXJX89F413\naBS94HQ4C1lgVR9NJZw/9UufVmOmRVjozuglbTZc3SjXTdU+sFzJFpU3YUDjJ09I\nEp53zPzbic5wVW9Dv3WHtzGN3X/HdiCD9PE88/nMf4erfbR+PPcMSCYwc53i/Vrw\niG57Wdd7AgMBAAECggEAPhgxtAul2l57YP5Y85mEL7vJU9hZyvWm6WJDn/PRh1Pf\nqepZWA8JXcNTJ0kS35DZ6bfMqCg5bQBadTCIxvapmx91f4UrPXf+pN9gQVwGvhb8\n+5GQf7smByM/RBKlIZ2P6ul0KmE3iukBC5v7gTy0hMr6hGl9a6t712erEX1W/zHa\nkjIAcGFOCTMdr7TXswOm4jhxb5wNX+IzCjFD7rgLBCL1H1buP8jqW4Icozg68f8y\nn/UP5cPBV9cDtBTOy+rUIttUnma2VqSlj+P1Y+cdAoctzjNo7u6Tf0i1PbHoBxaG\nXZLbSK6oXlmx3j1LkWmEXQx+nEzC0X57VupdxtPWyQKBgQDqdha9Wd31/yo0z3QY\ndHGYRBud2LV8WafU5zfwM7I8T7zQ5rjQYNgLKKj8sebWviRhg3ToL1rwVw5fqxpx\nhI1pbqXUO+Ygj5llGmM7aoo2Gs+UEsP8SzR/quyFEY543zk6m8MvDNf83u/LQpxB\nhzSMzolrM83ndgXGnBQM//N3xwKBgQDCiHfo0xTq4TEeWVvQjgVLohPASMkO8An0\nNRYx7BT7K3Qk2xLy3UszRqhP/Q+hmBGr/MBof9fM9ZX9PloRxyRUHV/ekCOYakue\nHZLQlpuzchRTHpmEbdEVYXUeLJXL/5mmUspVYB0NW9MA4SxjKkNnn5d77vgwuTvN\nydaiAPrqrQKBgBi2sdbrrhXL+6rtYxNg0sh2BWMyAI9G9AXhCWhsSpFI5mIur7Rn\nQN6MPqPELMzd5f/RV3VR6IgE5SL4aiUlD7LZUul4Ft2/xS9/BI6ywDbLxK2a/MWP\nN81tOtkb7KSZAvNGj+BZvjqNVUFbmBTgJ43gB2QhZcbMDffNY9+q6wLpAoGALlB1\nU+4lVcJlglc9/8pAQoCE+Uua6r6FcCnPbfVKDX6L+VZbDY1YIQ4admjGdu+QjYTw\nfjCpvTdfDVrx8Leb05bmw0NH5jqwZCWgVZ711OQ9KIjpfdjzZYa9boCo7VeeSjX9\nzBPE9vA8rUHu4nEmeR5K34vGdTs0nMsIGavoEUUCgYASvFH83qhOBZxvc5iYeKvg\naVUpHJ/tUeIb64tRX68+MpqghMohrKzugJXFkEmoxCdMaNVdVjw3HNJJSQfkNqyk\nePzjR3XIXn8dpPPZMXPYIn8eJO0y8BxFDfaxGBmHzQWwg/sLWUtb44joeLz4iZol\nkzBBhQv2U3A1jVsxNNLWqg==\n-----END PRIVATE KEY-----\n"
      }),
      databaseURL: "https://red-ciudadana-f31ed.firebaseio.com"
    })

    // Return our FirebaseAdmin object
    return FirebaseAdmin
  }
}

module.exports = FirebaseService