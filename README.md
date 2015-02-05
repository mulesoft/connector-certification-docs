# Anypoint Connector Certification Process 
## Public Documentation here
http://mulesoft.github.io/connector-certification-docs

## Preparing your environment
1. Install ruby.
2. Install coderay: `sudo gem install coderay`.
3. Install asciidoctor: `sudo gem install asciidoctor`.

## Building the docs locally:
1. Go to `[your_path]/connector-certification-docs/current` folder.
2. Run the following command: `asciidoctor -a stylesheet=assets/mule.css user-manual.adoc`.
3. Open the generated html file: `[your_path]/connector-certification-docs/current/user-manual.html`.
4. Be amazed.
