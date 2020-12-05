<template>
  <div id="eventregistration">
    <h2>Persons</h2>
    <table id="persons-table">
      <tr>
        <th>Name</th>
        <th>Events</th>
        <!-- ////////////////////////////////////////////////// -->
        <th>Payment ID</th>
        <th>AMOUNT ($)</th>
        <!-- ////////////////////////////////////////////////// -->
      </tr>
      <tr v-for="(person, i) in persons" v-bind:key="`person-${i}`">
        <td>{{ person.name }}</td>
        <td>
          <ul>
            <li
              v-for="(event, i) in person.eventsAttended"
              v-bind:key="`event-${i}`"
              style="list-style-type: disc"
            >
              <span class="registration-event-name">{{ event.name }}</span>
            </li>
          </ul>
        </td>
        <td v-if= "person.creditCard">{{ person.creditCard.accountNumber }}</td>
        <td v-if= "person.creditCard">{{ person.creditCard.amount }}</td>
        
        <!-- ////////////////////////////////////////////////// -->
      </tr>
      <tr>
        <td>
          <input
            id="create-person-person-name"
            type="text"
            v-model="newPerson"
            placeholder="Person Name"
          />
        </td>
        <!-- ///////////////////////Person Type /////////////////////////// -->
        <td>
          <select id="create-person-person-type" v-model="selectedPersonType">
            <option disabled value="">Please select one</option>
            <option v-for="(personType, i) in personType" v-bind:key="`personType-${i}`">
              {{ personType }}
            </option>
          </select>
        </td>
        <!-- ////////////////////////////////////////////////// -->
        <td>
          <button
            id="create-person-button"
            v-bind:disabled="!newPerson || !selectedPersonType"
            @click="createPerson(selectedPersonType, newPerson)"
          >
            Create Person
          </button>
        </td>
        <td></td>
        <td></td>
      </tr>
    </table>
    <span v-if="errorPerson" style="color: red">Error: {{ errorPerson }}</span>

    <hr />
    <h2>Events</h2>
    <table id="events-table">
      <tr>
        <th>Name</th>
        <th>Date</th>
        <th>Start</th>
        <th>End</th>
        <!-- ////////////////////////////////////////////////// -->
        <th style="color: red">Title</th>
        <!-- ////////////////////////////////////////////////// -->
      </tr>
      <tr
        v-for="(event, i) in events"
        v-bind:id="event.name"
        v-bind:key="`event-${i}`"
      >
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-name`">
          {{ event.name }}
        </td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-date`">
          {{ event.date }}
        </td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-starttime`">
          {{ event.startTime }}
        </td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-endtime`">
          {{ event.endTime }}
        </td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-title`">
          {{ event.title }}
        </td>
      </tr>

      <tr>
        <td>
          <input
            id="event-name-input"
            type="text"
            v-model="newEvent.name"
            placeholder="Event Name"
          />
        </td>
        <td>
          <input
            id="event-date-input"
            type="date"
            v-model="newEvent.date"
            placeholder="YYYY-MM-DD"
          />
        </td>
        <td>
          <input
            id="event-starttime-input"
            type="time"
            v-model="newEvent.startTime"
            placeholder="HH:mm"
          />
        </td>
        <td>
          <input
            id="event-endtime-input"
            type="time"
            v-model="newEvent.endTime"
            placeholder="HH:mm"
          />
        </td>
        <!-- ///////////////////////////////////////////////////////// -->
        <td>
          <input
            id="event-title-input"
            type="text"
            v-model="newEvent.title"
            placeholder="Theatre title"
          />
        </td>
        <!-- ///////////////////////////////////////////////////////// -->

        <td>
          <button
            id="event-create-button"
            v-bind:disabled="!newEvent.name"
            v-on:click="
              createEvent(newEvent);
            "
          >
            Create
          </button>
        </td>
      </tr>
    </table>
    <span id="event-error" v-if="errorEvent" style="color: red"
      >Error: {{ errorEvent }}</span
    >
    <hr />
    <h2>Registrations</h2>
    <label
      >Person:
      <select id="registration-person-select" v-model="selectedPerson">
        <option disabled value="">Please select one</option>
        <option v-for="(person, i) in persons" v-bind:key="`person-${i}`">
          {{ person.name }}
        </option>
      </select>
    </label>
    <label
      >Event:
      <select id="registration-event-select" v-model="selectedEvent">
        <option disabled value="">Please select one</option>
        <option v-for="(event, i) in events" v-bind:key="`event-${i}`">
          {{ event.name }}
        </option>
      </select>
    </label>
    <button
      id="registration-button"
      v-bind:disabled="!selectedPerson || !selectedEvent"
      @click="registerEvent(selectedPerson, selectedEvent)"
    >
      Register
    </button>
    <br />
    <span v-if="errorRegistration" style="color: red"
      >Error: {{ errorRegistration }}</span
    >
    <hr />
    <!-- ////////////////////////////ASSIGN PROMOTERS///////////////////////////// -->
    <h2>Assign Promoters</h2>
    <label
      >Promoter:
      <select id="assign-selected-promoter" v-model="selectedPromoter">
        <option disabled value="">Please select one</option>
        <option v-for="(promoter, i) in promoters" v-bind:key="`promoter-${i}`">
          {{ promoter.name }}
        </option>
      </select>
    </label>
    <label
      >Event:
      <select id="assign-selected-event-promoter" v-model="eventSelectedName">
        <option disabled value="">Please select one</option>
        <option v-for="(event, i) in events" v-bind:key="`event-${i}`">
          {{ event.name }}
        </option>
      </select>
    </label>
    <button
      id="assign-button-promoter"
      v-bind:disabled="!selectedPromoter || !eventSelectedName"
      @click="assignEvent(selectedPromoter, eventSelectedName)"
    >
      Assign
    </button>
    <br />
    <span v-if="errorAssign" style="color: red">Error: {{ errorAssign }}</span>
    <!-- ///////////////////////PAYMENT FOR REGISTRATION/////////////////////// -->
    <hr />
    <h2>Pay for Registration with CreditCard</h2>
    <label
      >Person:
      <select id="credit-card-person-select" v-model="personNameForPay">
        <option disabled value="">Please select one</option>
        <option v-for="(person, i) in persons" v-bind:key="`person-${i}`">
          {{ person.name }}
        </option>
      </select>
    </label>
    <label
      >Event:
      <select id="credit-card-event-select" v-model="eventNameForPay">
        <option disabled value="">Please select one</option>
        <option v-for="(event, i) in events" v-bind:key="`event-${i}`">
          {{ event.name }}
        </option>
      </select>
    </label>
    <br />
    <label
      >CreditCard iD:
      <input
        id="credit-card-id-input"
        type="text"
        v-model="selectedCreditCardAccountNumber"
        placeholder="Account number"
      />
    </label>
    <label
      >Amount:
      <input
        id="credit-card-amount-input"
        type="number"
        v-model="selectedAmount"
        placeholder="Account number"
      />
    </label>
    <br />
    <button
      id="credit-card-button"
      v-bind:disabled="
        !personNameForPay ||
        !eventNameForPay ||
        !selectedCreditCardAccountNumber ||
        !selectedAmount
      "
      @click="createCreditCardAndPay(selectedCreditCardAccountNumber, selectedAmount, personNameForPay, eventNameForPay)"
    >
      Make payment
    </button>
    <br />
    <span id="credit-card-error" v-if="errorCreditCardAndPay" style="color: red">Error: {{ errorCreditCardAndPay }}</span>
    <hr />
    <!-- ///////////////////////////////////////////////////////// -->
  </div>
</template>

<script src="./registration.js"></script>

<style>
#eventregistration {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background: #f2ece8;
  margin-top: 60px;
}
.registration-event-name {
  display: inline-block;
  width: 25%;
}
.registration-event-name {
  display: inline-block;
}
h1,
h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  text-align: left;
}
a {
  color: #42b983;
}
</style>
