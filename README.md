# Anypoint Connector Certification Process 

## Complete Certification Documentation here
http://mulesoft.github.io/complete-connector-certification-docs

## Basic Certification Documentation here
http://mulesoft.github.io/basic-connector-certification-docs 

## Preparing your environment
1. Install ruby.
2. Install bundler: `sudo gem install bundler`.	

## Building the docs locally:
1. Run `bundle install` to download all the dependencies needed.
2. Run `rake docs:build` (`rake docs:build['basic']` for the basic certification docs) 
3. An __HTML__ file will be generated with 2 new folders: __images and files__.
4. Open the __HTML__ generated file and there you go.
