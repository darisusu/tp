package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Age;
import seedu.address.model.person.Bodyfat;
import seedu.address.model.person.Deadline;
import seedu.address.model.person.Email;
import seedu.address.model.person.Gender;
import seedu.address.model.person.Goal;
import seedu.address.model.person.Height;
import seedu.address.model.person.Name;
import seedu.address.model.person.Paid;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Session;
import seedu.address.model.person.Weight;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String deadline;
    private final String goal;
    private final String height;
    private final String weight;
    private final String age;
    private final String gender;
    private final String bodyfat;
    private final String paid; // New field for payment status
    private final String session;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email,
                             @JsonProperty("address") String address,
                             @JsonProperty("goal") String goal,
                             @JsonProperty("height") String height,
                             @JsonProperty("weight") String weight,
                             @JsonProperty("age") String age,
                             @JsonProperty("gender") String gender,
                             @JsonProperty("deadline") String deadline,
                             @JsonProperty("paid") String paid,
                             @JsonProperty("session") String session,
                             @JsonProperty("bodyfat") String bodyfat,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.deadline = deadline;
        this.goal = goal;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.paid = paid;
        this.session = session;
        this.bodyfat = bodyfat;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        deadline = source.getDeadline().toStorageString();
        goal = source.getGoal() == null ? null : source.getGoal().value;
        height = source.getHeight() == null ? null : String.valueOf(source.getHeight().value);
        weight = source.getWeight() == null ? null : String.valueOf(source.getWeight().value);
        age = source.getAge() == null ? null : String.valueOf(source.getAge().value);
        gender = source.getGender() == null ? null : source.getGender().value;
        paid = source.getPaymentStatus().toString();
        bodyfat = source.getBodyfat() == null ? null : source.getBodyfat().toString();
        session = source.getSession().toStorageString();
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        final Phone modelPhone;
        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Phone.class.getSimpleName()));
        } else if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        } else {
            modelPhone = new Phone(phone);
        }

        final Email modelEmail;
        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Email.class.getSimpleName()));
        } else if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        } else {
            modelEmail = new Email(email);
        }

        final Address modelAddress;
        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Address.class.getSimpleName()));
        } else if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        } else {
            modelAddress = new Address(address);
        }

        final Deadline modelDeadline;
        if (deadline == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Deadline.class.getSimpleName()));
        } else {
            if (!Deadline.isValidDeadline(deadline)) {
                throw new IllegalValueException(Deadline.MESSAGE_CONSTRAINTS);
            }
            modelDeadline = Deadline.fromString(deadline);
        }

        final Goal modelGoal = goal == null ? null : new Goal(goal);

        final Height modelHeight;
        if (height == null) {
            modelHeight = null;
        } else if (!Height.isValidHeight(height)) {
            throw new IllegalValueException(Height.MESSAGE_CONSTRAINTS);
        } else {
            modelHeight = new Height(height);
        }

        final Weight modelWeight;
        if (weight == null) {
            modelWeight = null;
        } else if (!Weight.isValidWeight(weight)) {
            throw new IllegalValueException(Weight.MESSAGE_CONSTRAINTS);
        } else {
            modelWeight = new Weight(weight);
        }

        final Age modelAge;
        if (age == null) {
            modelAge = null;
        } else if (!Age.isValidAge(age)) {
            throw new IllegalValueException(Age.MESSAGE_CONSTRAINTS);
        } else {
            modelAge = new Age(age);
        }

        final Gender modelGender;
        if (gender == null) {
            modelGender = null;
        } else if (!Gender.isValidGender(gender)) {
            throw new IllegalValueException(Gender.MESSAGE_CONSTRAINTS);
        } else {
            modelGender = new Gender(gender);
        }

        final Paid modelPaid;
        if (paid == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Paid.class.getSimpleName()));
        } else if (!Paid.isValidPaid(paid)) {
            throw new IllegalValueException(Paid.MESSAGE_CONSTRAINTS);
        } else {
            modelPaid = new Paid(paid);
        }

        final Bodyfat modelBodyfat;
        if (bodyfat == null) {
            modelBodyfat = null;
        } else if (!Bodyfat.isValidBodyfat(bodyfat)) {
            throw new IllegalValueException(Bodyfat.MESSAGE_CONSTRAINTS);
        } else {
            modelBodyfat = new Bodyfat(bodyfat);
        }

        final Session modelSession;
        if (session == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Session.class.getSimpleName()));
        } else {
            try {
                modelSession = Session.fromString(session);
            } catch (IllegalArgumentException ex) {
                throw new IllegalValueException(ex.getMessage(), ex);
            }
        }

        final Set<Tag> modelTags = new HashSet<>(personTags);

        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelGoal,
                modelHeight, modelWeight, modelAge, modelGender, modelDeadline, modelPaid, modelBodyfat,
                modelSession, modelTags);
    }

}
