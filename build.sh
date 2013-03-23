#####
# Build Wink Website
#
#   Requirements : Define a CMS_HOME pointing to where you have installed 
# the cms build tool from https://svn.apache.org/repos/infra/websites/cms/build/
#

BUILD_HOME=$PWD

cd $CMS_HOME
export MARKDOWN_SOCKET=`pwd`/markdown.socket PYTHONPATH=`pwd`
python markdownd.py

cd $BUILD_HOME
perl $CMS_HOME/build_site.pl --source-base . --target-base ./target
