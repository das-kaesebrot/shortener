#include <iostream>
#include <stdlib.h>
#include <iterator>

#include <cgicc/Cgicc.h>
#include <cgicc/HTTPHTMLHeader.h>
#include <cgicc/HTMLClasses.h>

using namespace std;
using namespace cgicc;

int main(int argc, char *argv[])
{
   string website_title = "Kaesebrot's URL shortener";
   try {
      Cgicc cgi;

      // get Path Info
      string querystr = cgi.getEnvironment().getQueryString();

      // Send HTTP header
      cout << HTTPHTMLHeader() << endl;

      // Set up the HTML document
      cout << html() << head(title(website_title)) << endl;
      cout << body("Query string: " + querystr) << html() << endl;

      // switch case
   }
   catch(exception& e) {
      cout << HTTPHTMLHeader() << endl;
      cout << html() << head(title(website_title)) << endl;
      cout << body("An unknown exception has occured") << html() << endl;
   }
}