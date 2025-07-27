/**
 * Provide login security to applications running under the application
 * framework. In order for security to work correctly the tables defined in
 * <code>DefineSecurityTables.sql</code> must be run. The tables are defined a
 * database called production by default. Change the sql script to put them in
 * your preferred database.
 * 
 * Following is command to backup a database.<br>
 * mysqldump -u root -p -h localhost--databases production > security.sql<br>
 * 
 * <p>
 * This command will prompt for root's password and then write the database to
 * the specified file (for example security.sql).
 * 
 * @author neville
 * @version 4.1.0
 */
package application.security;