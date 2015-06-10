namespace :docs do
  desc 'prepare build'
  task :prebuild do
    Dir.mkdir './images' unless Dir.exists? 'images'
    Dir.glob("./docs/*/images/*").each do |image|
      FileUtils.copy(image, "images/" + File.basename(image))
    end
    Dir.mkdir './files' unless Dir.exists? './files'
    Dir.glob("./docs/*/files/*").each do |file|
      FileUtils.copy(file, "./files/" + File.basename(file))
    end

    Dir.glob("./files/*.asc").each do |file|
      `asciidoctor #{file}`
    end
  end


  task :build => :prebuild do

    puts "Converting to HTML..."
    `asciidoctor certification-docbook.asc -a stylesheet=theme/style.css`
    puts " -- HTML output at certification-docbook.html"

  end

  task :buildBasicCert => :prebuild do

    puts "Converting to HTML..."
    `asciidoctor basic-certification-docbook.asc -a stylesheet=theme/style.css`
    puts " -- HTML output at basic-certification-docbook.html"

  end

  task :package do
    Dir.mkdir './generated' unless Dir.exists? './generated'
    Dir.mkdir './generated/images' unless Dir.exists? './generated/images'
    Dir.glob("./images/*").each do |image|
      FileUtils.copy(image, "./generated/images/" + File.basename(image))
    end
    Dir.mkdir './generated/files' unless Dir.exists? './generated/files'
    Dir.glob("./files/*").each do |file|
      FileUtils.copy(file, "./generated/files/" + File.basename(file))
    end
    Dir.glob("./*.html").each do |file|
      FileUtils.copy(file, "./generated/"+File.basename(file));
    end
  end

  task :unpackage do
    `rm -rf ./images`
    `rm -rf ./files`
    Dir.glob("./*.html").each do |file|
      FileUtils.rm file
    end

    Dir.mkdir './images' unless Dir.exists? './images'
    Dir.glob("./generated/images/*").each do |image|
      FileUtils.copy(image, "./images/" + File.basename(image))
    end
    Dir.mkdir './files' unless Dir.exists? './files'
    Dir.glob("./generated/files/*").each do |file|
      FileUtils.copy(file, "./files/" + File.basename(file))
    end

    Dir.glob("./generated/*.html").each do |file|
      FileUtils.copy(file, File.basename(file))
    end

  end

  desc 'push generated documents to the repository'
  task :upload => [:build, :package ] do
    puts "Uploading generated documentation"
    `git checkout gh-pages -f`
     Rake::Task["docs:unpackage"].invoke
    `git add *.html && git add images/ && git add files/ && git commit -m 'Update Docs'`
    `git push origin gh-pages -f`
    `git checkout develop`

  end

  desc 'clean out generated formats'
  task :clean do
    `rm -rf generated`
    `rm -rf images`
    `rm -rf files`
    Dir.glob("./*.html").each do |file|
      FileUtils.rm file
    end
  end

end
