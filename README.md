Task description
================

**Required technologies / libraries**

1. Java
2. Maven
3. Cucumber
4. Selenium Webdriver
5. Rest Assured
6. Docker

**Task 1.**

✅ GUI test that will perform a search for a package on the InPost website by its number and check if it has a status as expected.
List of packages with statuses:
- no: 520113014230722029585646, expected status: Delivered
- no: 520107010499997005638120, expected status: Passed for delivery
- No: 523000016696115042036670, expected status: The label was cancelled
- No: 520000011395200025754311, expected status: Delivered

**Task 2.**

✅ API test that will perform a Parcel Lockers search for a city (several cities) and save the data of the returned Parcel Lockers (name, postal code, coordinates) to the file 'parcellockers.{city}.json'.

**Guidelines:**

- ✅ publish the repository with the solved tasks on github.
- ✅ run tests from a Docker image
- ✅ when running tests it should be possible to indicate whether you want to run only GUI , API or all tests
- ✅ test results should produce html report
- ✅ GUI test report should contain screenshot in case of unsuccessful test result
- ✅ (*) simulation of running tests on few environments
- ✅ (*) docker-compose.yml

**Deadline**: usually 3 working days is enough, please let me know if you need more time.


In case of any questions regarding the task, do not hesitate to reach out to us.
However please note that all needed informations are in the task and if you'd have any assumptions, feel free to document them.

You may send us your solution as a .zip or a link to your repository.


Run tests
=========

Before running locally
----------------------

API tests are in `inpost-api-tests` module and GUI tests are in `inpost-gui-tests` module. In `inpost-common` there is common code - it should already be built and installed locally, before running tests:

    cd ./inpost-common 
    mvn clean install 
    cd ..

Run locally
-----------

All tests, on default environment: 
    
    mvn clean test --fail-at-end

To run tests on environemnt add: 
    
    -D environment=prod
    -D environment=sandbox
    -D environment=sandboxpl

To run API tests only, add: 
    
    -D cucumber.filter.tags="@api"

or: 
    
    cd ./inpost-api-tests
    mvn clean test

To run UI tests only, add: 
    
    -D cucumber.filter.tags="@gui"

or: 

    cd ./inpost-gui-tests
    mvn clean test

To change browser, add: 
    
    -D browser=chrome
    -D browser=firefox

For example, API tests on `prod` environment: 
    
    mvn clean test -D environment=prod -D cucumber.filter.tags="@api" --fail-at-end
    mvn clean test -D environment=prod -D cucumber.filter.tags="@gui" -D browser=chrome --fail-at-end

Run in Docker
-------------

### Run in standalone browser in Docker: 
    
    docker pull selenium/standalone-firefox
    docker run -d -p 4444:4444 -p 7900:7900 --shm-size="2g" selenium/standalone-firefox

Check the grid at http://localhost:4444/ui/, see the browser action http://localhost:7900. 

Run tests, for example:
    
     mvn clean test -D environment=prod -D cucumber.filter.tags="@gui" -D grid=http://localhost:4444 --fail-at-end


### Run in Selenium grid in Docker:
    
    docker compose -f docker-compose.yml up

Check the grid - same as above - at http://localhost:4444/ui/, see the browser action http://localhost:7900.

Run tests - same as above: 
    
    mvn clean test -D environment=prod -D grid=http://localhost:4444 --fail-at-end

To stop: 
    
    Ctrl+C
    docker compose -f docker-compose.yml down


Run from a Docker image
-----------------------

Build the image: 
    
    docker build -t inpota .

In the meantime, run the grid: 
    
    docker compose -f docker-compose.yml up

Run the container: 
    
    docker kill inpota
    docker run --rm -t -d --name inpota inpota

Then run tests of your choice, for example: 
    
    docker exec inpota bash -c "./mvnw clean test --fail-at-end -D grid=http://host.docker.internal:4444"

Or [run tests, collect results, open reports](#run-tests-collect-results-open-reports). 

To clean up after runs: 
    
    docker compose -f docker-compose.yml down
    docker kill inpota


Open reports
============

Locally
-------

    open inpost-ui-tests/target/cucumber.html
    open inpost-api-tests/target/cucumber.html

From Docker container run
-------------------------

### Collect results locally:

    mkdir ./results/api/prod
    docker cp inpota:inpost-api-tests/target ./results/prod/api
    mkdir ./results/gui/prod
    docker cp inpota:inpost-api-tests/target ./results/prod/gui

### Run tests, collect results, open reports

You can run `docker_tests_run.sh` script which runs tests, collects them locally and opens reports. For example: 

    chmod u+x docker_tests_run.sh 
    ./docker_tests_run.sh prod
    ./docker_tests_run.sh sandbox @api
    ./docker_tests_run.sh sandboxpl @gui


Assumptions:
============

User is in charge of inputting city name in scenario. If they provide it somehow incorrectly, that's how it will be executed within API. 

Ad. Task 1. Test data and desired effect don't match - left failed tests. 

Ad. Task 2. Some data fails tests because it is bad - left failed tests. This is not deterministic - there are times when that bad data is not present on environment (even on prod) 🙃 

