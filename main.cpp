#include <iostream>
#include <stdlib.h>
#include <iterator>
#include <sstream> 

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
      string querystr = cgi.getEnvironment().getQueryString();
      std::stringstream queryss(querystr);

      // Send HTTP header
      cout << HTTPHTMLHeader() << endl;

      // Set up the HTML document
      cout << html() << head(title(website_title)) << endl;

      // Get query string (I know, it's a hack)
      string s;
      getline(queryss, s, '=');
      getline(queryss, s, '=');
      
      // cout << "Found: " << out << endl;
      cout << body("Query string: " + s) << html() << endl;
      
   }
   catch(exception& e) {
      cout << HTTPHTMLHeader() << endl;
      cout << html() << head(title(website_title)) << endl;
      cout << body("An unknown exception has occured") << html() << endl;
   }
}