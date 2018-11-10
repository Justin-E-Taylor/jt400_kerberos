# jt400_kerberos
jt400_kerberos is an R&amp;D app to explore Kerberos authentication with jt400.jar

This is a simple example of establishing a JDBC connection to a remote IBM i using Kerberos authentiation.  Ironically enough, it requires running kinit from the client Java bin folder, despite the fact that other clients (iNav, 5250, NetServer, ODBC, HTTP) do not require this step.
