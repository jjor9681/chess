package model;

/* Record class for UserData with required fields from documentation.
 * This is the equivalent of making a whole class with fields and this.field equals statements.
 */
public record UserData (
        String username,
        String password,
        String email
){}

