package com.benjie.librarymanagement.entity;

/*
 * Created by OPARA BENJAMIN
 * On 12/13/2020 - 8:28 PM
 */

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@MappedSuperclass
@NamedQuery(name = LibraryMember.FIND_MEMBER_BY_NAME, query = "select l from LibraryUser l where l.firstName like :firstName or l.lastName like :lastName")
@NamedQuery(name = LibraryMember.FIND_MEMBER_BY_ID, query = "select l from LibraryUser l where l.id = :id")
public abstract class LibraryMember implements Serializable {

    public static final String FIND_MEMBER_BY_NAME = "LibraryMember.findMemberByName";
    public static final String FIND_MEMBER_BY_ID = "LibraryMember.findMemberById";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "First name cannot be empty")
    @Size(min = 2, message = "First Name must contain at least two characters")
    private String firstName;

    @NotNull(message = "Last name cannot be empty")
    @Size(min = 2, message = "Last Name must contain at least two characters")
    private String lastName;

    @NotNull(message = "Birthday cannot be empty")
    @JsonbDateFormat(value = "yyyy-MM-dd")
    @Past(message = "Birthday must be in the past")
    private LocalDate birthday;

    @NotNull(message = "Email must not be empty")
    @Email(message = "Email must be in the form user@domain.com")
    private String email;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least six (6) character long")
    private String password;

    private String passwordSalt;


    @ManyToMany
    @JoinTable(name = "jnd_member_book",
            joinColumns = @JoinColumn(name = "borrower_fk"),
            inverseJoinColumns = @JoinColumn(name = "book_fk"))
    private Collection<Book> currentHolder = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public Collection<Book> getCurrentHolder() {
        return currentHolder;
    }

    public void setCurrentHolder(Collection<Book> currentHolder) {
        this.currentHolder = currentHolder;
    }
}
