const config = require('./config.js');

module.exports = {
  'Test Create Theatre': function (client) {
    const event = {
      title: 'Hamlet',
      name: 'Theatre 1',
      date: '0020301010',
      startTime: '08:00:00',
      endTime: '11:00:00'
    };
      // Replaces spaces by dashes (ID's can't have spaces)
    const id_name = event.name.replace(/\s/g, '-');

    client
      .url(client.globals.devServerURL)
      .waitForElementVisible('body', config.time.visible)
      .assert.visible(config.id.titleInput)
      .setValue(config.id.titleInput, event.title)
      .pause(config.time.pause)
      .assert.visible(config.id.nameInput)
      .setValue(config.id.nameInput, event.name)
      .pause(config.time.pause)
      .assert.visible(config.id.dateInput)
      .setValue(config.id.dateInput, `${event.date.day}-${event.date.month}-${event.date.year}`)
      .pause(config.time.pause)
      .assert.visible(config.id.starttimeInput)
      .setValue(config.id.starttimeInput, event.startTime)
      .pause(config.time.pause)
      .assert.visible(config.id.endtimeInput)
      .setValue(config.id.endtimeInput, event.endTime)
      .pause(config.time.pause)
      .click(config.id.createButton)
      .pause(config.time.pause)
      .assert.containsText(`#${id_name}-title`, event.title)
      .assert.containsText(`#${id_name}-name`, event.name)
      .end();
  },
  'Test Create Generic Event': function (client) {
    const event = {
      title: '--',
      name: 'Generic Event for Theatre',
      date: '0020301010',
      startTime: '08:00:00',
      endTime: '11:00:00'
    };
    // Replaces spaces by dashes (ID's can't have spaces)
    const id_name = event.name.replace(/\s/g, '-');

    client
      .url(client.globals.devServerURL)
      .waitForElementVisible('body', config.time.visible)
      .assert.visible(config.id.nameInput)
      .setValue(config.id.nameInput, event.name)
      .pause(config.time.pause)
      .assert.visible(config.id.dateInput)
      .setValue(config.id.dateInput, `${event.date.day}-${event.date.month}-${event.date.year}`)
      .pause(config.time.pause)
      .assert.visible(config.id.starttimeInput)
      .setValue(config.id.starttimeInput, event.startTime)
      .pause(config.time.pause)
      .assert.visible(config.id.endtimeInput)
      .setValue(config.id.endtimeInput, event.endTime)
      .pause(config.time.pause)
      .click(config.id.createButton)
      .pause(config.time.pause)
      .assert.containsText(`#${id_name}-title`, event.title)
      .assert.containsText(`#${id_name}-name`, event.name)
      .end();
  },
  'Test Create Existing Theatre': function (client) {
    const event = {
      title: 'Hamlet',
      name: 'Theatre 1',
      date: '0020301010',
      startTime: '08:00:00',
      endTime: '11:00:00'
    };
    const error = 'Event has already been created!';

    client
      .url(client.globals.devServerURL)
      .waitForElementVisible('body', config.time.visible)
      .assert.visible(config.id.titleInput)
      .setValue(config.id.titleInput, event.title)
      .pause(config.time.pause)
      .assert.visible(config.id.nameInput)
      .setValue(config.id.nameInput, event.name)
      .pause(config.time.pause)
      .assert.visible(config.id.dateInput)
      .setValue(config.id.dateInput, `${event.date.day}-${event.date.month}-${event.date.year}`)
      .pause(config.time.pause)
      .assert.visible(config.id.starttimeInput)
      .setValue(config.id.starttimeInput, event.startTime)
      .pause(config.time.pause)
      .assert.visible(config.id.endtimeInput)
      .setValue(config.id.endtimeInput, event.endTime)
      .pause(config.time.pause)
      .click(config.id.createButton)
      .pause(config.time.pause)
      .assert.containsText(config.id.error, error)
      .end();
  },
  'Test Create Theatre Empty Time': function (client) {
    const event = {
      title: 'Hamlet',
      name: 'Theatre 2',
      date: '0020301010'
    };
    const error = 'Error';

    client
      .url(client.globals.devServerURL)
      .waitForElementVisible('body', config.time.visible)
      .assert.visible(config.id.titleInput)
      .setValue(config.id.titleInput, event.title)
      .pause(config.time.pause)
      .assert.visible(config.id.nameInput)
      .setValue(config.id.nameInput, event.name)
      .pause(config.time.pause)
      .assert.visible(config.id.dateInput)
      .setValue(config.id.dateInput, `${event.date.day}-${event.date.month}-${event.date.year}`)
      .pause(config.time.pause)
      .assert.visible(config.id.starttimeInput)
      .sendKeys(config.id.starttimeInput, client.Keys.BACK_SPACE)
      .sendKeys(config.id.starttimeInput, client.Keys.RIGHT_ARROW)
      .sendKeys(config.id.starttimeInput, client.Keys.BACK_SPACE)
      .pause(config.time.pause)
      .assert.visible(config.id.endtimeInput)
      .sendKeys(config.id.endtimeInput, client.Keys.BACK_SPACE)
      .sendKeys(config.id.endtimeInput, client.Keys.RIGHT_ARROW)
      .sendKeys(config.id.endtimeInput, client.Keys.BACK_SPACE)
      .pause(config.time.pause)
      .click(config.id.createButton)
      .pause(config.time.pause)
      .assert.containsText(config.id.error, error)
      .end();
  },
  'Test Create Disabled Empty Name': function (client) {
    client
    .url(client.globals.devServerURL)
    .waitForElementVisible('body', config.time.visible)
    .assert.visible(config.id.nameInput)
    .assert.attributeEquals(config.id.createButton, 'disabled', 'true')
    .end();
  }
};
