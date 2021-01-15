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
    try {
      Cgicc cgi;

      // Send HTTP header
      cout << HTTPHTMLHeader() << endl;

      // Set up the HTML document
      cout << html() << head(title("cgicc example")) << endl;
      cout << body("Test") << html() << endl;
   }
   catch(exception& e) {
      // handle any errors - omitted for brevity
   }
}