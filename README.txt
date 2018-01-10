Community Members,

We are pleased to announce the availability of a
kXML-RPC maintenance release, version 1.1.2.

This release updates some of the previous release's (1.1.1) 
source code, providing important bug fixes. Please see the 
changelog below for further details.

David Johnson (kXML-RPC team)

Significant Changes
===================
A number of important fixes have been contributed to address the following bugs

- The two-argument constructor for XmlRpcClient did not 
correctly handle host locations that included 
subdirectories
- Serializing Array datatype did not implement <data> start 
and end tags
- Parsing strings where no <string> tags were used threw an 
exception
- Date/time values were not implemented to the correct 
XML-RPC specification in both serialization and parsing. 
This has been fixed by providing a new implementation of 
IsoDate to replace org.kobjects.isodate.IsoDate

Known Issues
============
This release has been tested against a service deployed
using the Redstone XML-RPC and Apache XML-RPC (ws-xmlrpc)
libraries. The following issues are already known so please 
DO NOT report these as bugs at this time as they have 
already been identified and solutions are being worked on.

- No support for XML-RPC double datatype
- Exception thrown when parsing <fault> responses. This is 
thought to occur because fault messages contain HTML tags 
which the parser tries to parse as XML.

Documentation
=============
JavaDocs can be accessed online at the following:
http://kxmlrpc.sourceforge.net/javadoc/

Downloading and Installing
==========================
kXML-RPC version 1.1 can be installed from :

Source: 
http://downloads.sourceforge.net/kxmlrpc/kxmlrpc1.1.2-src.zip

Binary (full, already includes dependant classes):
http://downloads.sourceforge.net/kxmlrpc/kxmlrpc1.1.2.jar

Binary (minimal, requires kxml2 binary on classpath):
http://downloads.sourceforge.net/kxmlrpc/kxmlrpc1.1.2-min.jar

or alternatively directly access the source via CVS :

http://sourceforge.net/cvs/?group_id=194336

Remember to use the cvs tag "KXMLRPC_1_1" for this 
release. Also please bear in mind that if you are attempting
to build from the source yourself, you will require the 
latest kxml2 binary or source library available from 
http://kxml.sourceforge.net
