package geekbrains.spring.homework.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class LibraryError {
    private int status;
    private String message;
    private Date timestamp;

    public LibraryError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
