/* eslint-disable semi */
/* eslint-disable indent */
/* eslint-disable no-trailing-spaces */
var config = require('./config.json')
var testData = {
        'validPaymentData': {
                'personName': 'Percy-creditCard',
                'eventName': 'Soccer-creditCard',
                'startDate': '12/04/2019',
                'startTime': '8:00 AM',
                'endDate': '10:00 AM',
                'paymentType': 'creditCard',
                'amount': '100',
                'accountNumber': '8888-2343'
            },
            'InvalidIdTestData': {
                'personName': 'Percy1-creditCard',
                'eventName': 'Soccer1-creditCard',
                'startDate': '12/04/2019',
                'startTime': '8:00 AM',
                'endDate': '10:00 AM',
                'paymentType': 'creditCard',
                'amount': '100',
                'accountNumber': 'ABCD-ADFD'
            },
            'InvalidAmountTestData': {
                'personName': 'Percy2-creditCard',
                'eventName': 'Soccer2-creditCard',
                'startDate': '12/04/2019',
                'startTime': '8:00 AM',
                'endDate': '10:00 AM',
                'paymentType': 'creditCard',
                'amount': '-100',
                'accountNumber': '9877-2343'
            },
            'TestUpdatePaymentData': {
                'personName': 'Percy3-creditCard',
                'eventName': 'Soccer3-creditCard',
                'startDate': '12/04/2019',
                'startTime': '8:00 AM',
                'endDate': '10:00 AM',
                'paymentType': 'creditCard',
                'amount': '100',
                'accountNumber': '1111-2343',
                'newAccountNumber': '6666-9999',
                'newAmount': '200'
            }
}
module.exports = {
  'Test credit card Pay: 01 Component Existance': function (browser) {
    browser
        .url(config.url)
        .waitForElementVisible('body', 1500).pause(1500);
        // expect visible tables
        browser.expect.element(config.componentIDs.registrationTable).to.be.visible;
        browser.expect.element(config.componentIDs.eventTable).to.be.visible;

        // expect inputs to be visible
        browser.expect.element(config.componentIDs.personField).to.be.visible.and.to.be.an('input');
        browser.expect.element(config.componentIDs.eventField).to.be.visible.and.to.be.an('input');
        browser.expect.element(config.componentIDs.paymentId).to.be.visible.and.to.be.an('input');
        browser.expect.element(config.componentIDs.eventDate).to.be.visible.and.to.be.an('input')
                .and.have.attribute('type').equals('date');
        browser.expect.element(config.componentIDs.eventStartTime).to.be.visible.and.to.be.an('input')
                .and.have.attribute('type').equals('time');
        browser.expect.element(config.componentIDs.eventEndTime).to.be.visible.and.to.be.an('input')
                .and.have.attribute('type').equals('time');
        browser.expect.element(config.componentIDs.paymentAmount).to.be.visible.and.to.be.an('input');

       // expect visible selections
        browser.expect.element(config.componentIDs.registrationPerson).to.be.visible
        browser.expect.element(config.componentIDs.registrationEvent).to.be.visible
        browser.expect.element(config.componentIDs.paymentPerson).to.be.visible
        browser.expect.element(config.componentIDs.paymentEvent).to.be.visible

        // // expect visible buttons
        browser.expect.element(config.componentIDs.personButton).to.be.visible;
        browser.expect.element(config.componentIDs.eventButton).to.be.visible;
        browser.expect.element(config.componentIDs.registrationButton).to.be.visible;
        browser.expect.element(config.componentIDs.paymentButton).to.be.visible;

        // end test
        browser.end();
    },
    'Test credit card: 02 Valid Payment': function (browser) {
        browser
        .url(config.url)
        .waitForElementVisible('body', 1500).pause(1500);
        // step1: add a person
        browser.setValue(config.componentIDs.personField, testData.validPaymentData.personName)
               .click(config.componentIDs.personButton).pause(1500);
        browser.assert.containsText(config.componentIDs.registrationTable, testData.validPaymentData.personName);

        // step2: add an event
        browser.setValue(config.componentIDs.eventField, testData.validPaymentData.eventName)
                .setValue(config.componentIDs.eventDate, testData.validPaymentData.startDate)
                .setValue(config.componentIDs.eventStartTime, testData.validPaymentData.startTime)
                .setValue(config.componentIDs.eventEndTime, testData.validPaymentData.endDate)
                .click(config.componentIDs.eventButton).pause(1500);

        // step3: register person
        browser.setValue(config.componentIDs.registrationEvent, testData.validPaymentData.eventName)
                .setValue(config.componentIDs.registrationPerson, testData.validPaymentData.personName)
                .click(config.componentIDs.registrationButton).pause(1500);
        browser.assert.containsText(config.componentIDs.registrationTable, testData.validPaymentData.eventName);

        // step4: update payment
        // first get original table data
        browser.getText(config.componentIDs.registrationTable, function (result) {
                var originalText = result.value.toLowerCase().replace(/\s/g, '');
                browser.setValue(config.componentIDs.paymentPerson, testData.validPaymentData.personName)
                browser.setValue(config.componentIDs.paymentEvent, testData.validPaymentData.eventName)
                        .setValue(config.componentIDs.paymentId, testData.validPaymentData.accountNumber)
                        .setValue(config.componentIDs.paymentAmount, testData.validPaymentData.amount)
                        .click(config.componentIDs.paymentButton).pause(1500);

                // get text from table and change it to be lower case and no space (adopt different convension of payment type)
                browser.getText(config.componentIDs.registrationTable, function (result) {
                    // /\s/g matches all kind of spaces
                    var newText = result.value.toLowerCase().replace(/\s/g, '');

                    // remove original text from new table text
                    var diff = newText.replace(originalText, '');
                    // check existance of data
                    var containID = diff.indexOf(testData.validPaymentData.accountNumber.toLowerCase().replace(/\s/g, '')) >= 0;
                    var containAmount = diff.indexOf(testData.validPaymentData.amount.toLowerCase().replace(/\s/g, '')) >= 0;

                    browser.assert.equal(containID, true);
                    browser.assert.equal(containAmount, true);
                    browser.end()
                })
        });
    },
    'Test credit card: 03 Invalid ID': function (browser) {
        browser
        .url(config.url)
        .waitForElementVisible('body', 1500).pause(1500);
        // step1: add a person
        browser.setValue(config.componentIDs.personField, testData.InvalidIdTestData.personName)
               .click(config.componentIDs.personButton).pause(1500);
        browser.assert.containsText(config.componentIDs.registrationTable, testData.InvalidIdTestData.personName);

        // step2: add an event
        browser.setValue(config.componentIDs.eventField, testData.InvalidIdTestData.eventName)
                .setValue(config.componentIDs.eventDate, testData.InvalidIdTestData.startDate)
                .setValue(config.componentIDs.eventStartTime, testData.InvalidIdTestData.startTime)
                .setValue(config.componentIDs.eventEndTime, testData.InvalidIdTestData.endDate)
                .click(config.componentIDs.eventButton).pause(1500);

        // step3: register person
        browser.setValue(config.componentIDs.registrationEvent, testData.InvalidIdTestData.eventName)
                .setValue(config.componentIDs.registrationPerson, testData.InvalidIdTestData.personName)
                .click(config.componentIDs.registrationButton).pause(1500);
        browser.assert.containsText(config.componentIDs.registrationTable, testData.InvalidIdTestData.eventName);

        // step4: update payment, expect error, and no change in registration table
        browser.getText(config.componentIDs.registrationTable, function (result) {
            var valueBefore = result.value;
            browser.setValue(config.componentIDs.paymentPerson, testData.InvalidIdTestData.personName)
            browser.setValue(config.componentIDs.paymentEvent, testData.InvalidIdTestData.eventName)
                    .setValue(config.componentIDs.paymentId, testData.InvalidIdTestData.accountNumber)
                    .setValue(config.componentIDs.paymentAmount, testData.InvalidIdTestData.amount)
                    .click(config.componentIDs.paymentButton).pause(1500);

            // check that the error element is visible and the table content does not change
            browser.expect.element(config.componentIDs.paymentError).to.be.visible;
            browser.getText(config.componentIDs.registrationTable, function (result) {
                browser.assert.equal(valueBefore, result.value);
                browser.end()
            })
        })
    },
    'Test credit card: 04 Invalid Amount': function (browser) {
        browser
        .url(config.url)
        .waitForElementVisible('body', 1500).pause(1500);
        // step1: add a person
        browser.setValue(config.componentIDs.personField, testData.InvalidAmountTestData.personName)
               .click(config.componentIDs.personButton).pause(1500);
        browser.assert.containsText(config.componentIDs.registrationTable, testData.InvalidAmountTestData.personName);

        // step2: add an event
        browser.setValue(config.componentIDs.eventField, testData.InvalidAmountTestData.eventName)
                .setValue(config.componentIDs.eventDate, testData.InvalidAmountTestData.startDate)
                .setValue(config.componentIDs.eventStartTime, testData.InvalidAmountTestData.startTime)
                .setValue(config.componentIDs.eventEndTime, testData.InvalidAmountTestData.endDate)
                .click(config.componentIDs.eventButton).pause(1500);

        // step3: register person
        browser.setValue(config.componentIDs.registrationEvent, testData.InvalidAmountTestData.eventName)
                .setValue(config.componentIDs.registrationPerson, testData.InvalidAmountTestData.personName)
                .click(config.componentIDs.registrationButton).pause(1500);
        browser.assert.containsText(config.componentIDs.registrationTable, testData.InvalidAmountTestData.eventName);

        // step4: update payment, expect error, and no change in registration table
        browser.getText(config.componentIDs.registrationTable, function (result) {
            var valueBefore = result.value;
            browser.setValue(config.componentIDs.paymentPerson, testData.InvalidAmountTestData.personName)
            browser.setValue(config.componentIDs.paymentEvent, testData.InvalidAmountTestData.eventName)
                    .setValue(config.componentIDs.paymentId, testData.InvalidAmountTestData.accountNumber)
                    .setValue(config.componentIDs.paymentAmount, testData.InvalidAmountTestData.amount)
                    .click(config.componentIDs.paymentButton).pause(1500);

            // check that the error element is visible and the table content does not change
            browser.expect.element(config.componentIDs.paymentError).to.be.visible;
            browser.getText(config.componentIDs.registrationTable, function (result) {
                browser.assert.equal(valueBefore, result.value);
                browser.end()
            })
        })
    },
    'Test credit card: 05 Update Payment': function (browser) {
        browser
        .url(config.url)
        .waitForElementVisible('body', 1500).pause(1500);
        // step1: add a person
        browser.setValue(config.componentIDs.personField, testData.TestUpdatePaymentData.personName)
               .click(config.componentIDs.personButton).pause(1500);
        browser.assert.containsText(config.componentIDs.registrationTable, testData.TestUpdatePaymentData.personName);

        // step2: add an event
        browser.setValue(config.componentIDs.eventField, testData.TestUpdatePaymentData.eventName)
                .setValue(config.componentIDs.eventDate, testData.TestUpdatePaymentData.startDate)
                .setValue(config.componentIDs.eventStartTime, testData.TestUpdatePaymentData.startTime)
                .setValue(config.componentIDs.eventEndTime, testData.TestUpdatePaymentData.endDate)
                .click(config.componentIDs.eventButton).pause(1500);

        // step3: register person
        browser.setValue(config.componentIDs.registrationEvent, testData.TestUpdatePaymentData.eventName)
                .setValue(config.componentIDs.registrationPerson, testData.TestUpdatePaymentData.personName)
                .click(config.componentIDs.registrationButton).pause(1500);
        browser.assert.containsText(config.componentIDs.registrationTable, testData.TestUpdatePaymentData.eventName);

        // step4: update payment
        // first get original table data
        browser.getText(config.componentIDs.registrationTable, function (result) {
                var originalText = result.value.toLowerCase().replace(/\s/g, '');
                browser.setValue(config.componentIDs.paymentPerson, testData.TestUpdatePaymentData.personName)
                browser.setValue(config.componentIDs.paymentEvent, testData.TestUpdatePaymentData.eventName)
                        .setValue(config.componentIDs.paymentId, testData.TestUpdatePaymentData.accountNumber)
                        .setValue(config.componentIDs.paymentAmount, testData.TestUpdatePaymentData.amount)
                        .click(config.componentIDs.paymentButton).pause(1500);

                // Update Payment
                browser.setValue(config.componentIDs.paymentPerson, testData.TestUpdatePaymentData.personName)
                        .setValue(config.componentIDs.paymentEvent, testData.TestUpdatePaymentData.eventName)
                        .setValue(config.componentIDs.paymentId, testData.TestUpdatePaymentData.newAccountNumber)
                        .setValue(config.componentIDs.paymentAmount, testData.TestUpdatePaymentData.newAmount)
                        .click(config.componentIDs.paymentButton).pause(1500);
                // get text from table and change it to be lower case and no space (adopt different convension of payment type)
                browser.getText(config.componentIDs.registrationTable, function (result) {
                    // /\s/g matches all kind of spaces
                    var newText = result.value.toLowerCase().replace(/\s/g, '');

                    // remove original text from new table text
                    var diff = newText.replace(originalText, '');
                    // check existance of data
                    var containID = diff.indexOf(testData.TestUpdatePaymentData.newAccountNumber.toLowerCase().replace(/\s/g, '')) >= 0;
                    var containAmount = diff.indexOf(testData.TestUpdatePaymentData.newAmount.toLowerCase().replace(/\s/g, '')) >= 0;

                    browser.assert.equal(containID, true);
                    browser.assert.equal(containAmount, true);
                    browser.end()
                })
        });
    }
}
