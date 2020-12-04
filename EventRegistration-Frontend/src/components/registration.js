import _ from 'lodash';
import axios from 'axios';
let config = require('../../config');

let backendConfigurer = function () {
  switch (process.env.NODE_ENV) {
    case 'testing':
    case 'development':
      return 'http://' + config.dev.backendHost + ':' + config.dev.backendPort;
    case 'production':
      return 'https://' + config.build.backendHost + ':' + config.build.backendPort;
  }
}

let backendUrl = backendConfigurer();

let AXIOS = axios.create({
  baseURL: backendUrl
  // headers: {'Access-Control-Allow-Origin': frontendUrl}
});

export default {
  name: 'eventregistration',
  data() {
    return {
      persons: [],
      events: [],
      theatres: [],
      newPerson: '',
      personType: 'Person',
      newEvent: {
        name: '',
        date: '2017-12-08',
        startTime: '09:00',
        endTime: '11:00'
      },
      newTheatre: {
        name: '',
        date: '2017-12-08',
        startTime: '09:00',
        endTime: '11:00',
        title: ''
      },
      selectedPerson: '',
      selectedEvent: '',
      errorPerson: '',
      errorEvent: '',
      errorRegistration: '',
      response: [],
    }
  },
  created: function () {
    // Initializing persons
    AXIOS.get('/persons')
    .then(response => {
      this.persons = response.data;
      this.persons.forEach(person => this.getRegistrations(person.name))
    })
    .catch(e => {this.errorPerson = e});

    AXIOS.get('/events').then(response => {this.events = response.data}).catch(e => {this.errorEvent = e});
    AXIOS.get('/theatres').then(response => {this.theatres = response.data}).catch(e => {this.errorEvent = e});

  },

  methods: {

    createPerson: function (personType, personName) {
      AXIOS.post('/persons/'.concat(personName), {}, {})
      .then(response => {
        this.persons.push(response.data);
        this.errorPerson = '';
        this.newPerson = '';
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.errorPerson = e;
        console.log(e);
      });
    },

    createEvent: function (newEvent) {
      let url = '';

      AXIOS.post('/events/'.concat(newEvent.name), {}, {params: newEvent})
      .then(response => {
        this.events.push(response.data);
        this.errorEvent = '';
        this.newEvent.name = this.newEvent.make = this.newEvent.movie = this.newEvent.company = this.newEvent.artist = this.newEvent.title = '';
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.errorEvent = e;
        console.log(e);
      });
    },

    createTheatre: function (newTheatre) {
      let url = '';

      AXIOS.post('/theatres/'.concat(newTheatre.name), {}, {params: newTheatre})
      .then(response => {
        this.theatres.push(response.data);
        this.errorEvent = '';
        this.newTheare.name = this.newTheatre.make = this.newTheatre.movie = this.newTheatre.company = this.newTheatre.artist = this.newTheatre.title = '';
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.errorEvent = e;
        console.log(e);
      });
    },

    registerEvent: function (personName, eventName) {
      let event = this.events.find(x => x.name === eventName);
      let person = this.persons.find(x => x.name === personName);
      let params = {
        person: person.name,
        event: event.name
      };

      AXIOS.post('/register', {}, {params: params})
      .then(response => {
        person.eventsAttended.push(event)
        this.selectedPerson = '';
        this.selectedEvent = '';
        this.errorRegistration = '';
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.errorRegistration = e;
        console.log(e);
      });
      this.persons.forEach(person => this.getRegistrations(person.name))
    },

    getRegistrations: function (personName) {
      AXIOS.get('/events/person/'.concat(personName))
      .then(response => {
        if (!response.data || response.data.length <= 0) return;

        let indexPart = this.persons.map(x => x.name).indexOf(personName);
        this.persons[indexPart].eventsAttended = [];
        response.data.forEach(event => {
          this.persons[indexPart].eventsAttended.push(event);
        });
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        console.log(e);
      });
    },
  }
}
