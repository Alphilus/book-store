package com.example.bookstore;

import com.example.bookstore.entity.*;
import com.example.bookstore.model.enums.BookType;
import com.example.bookstore.model.enums.ParentGenres;
import com.example.bookstore.model.enums.UserRole;
import com.example.bookstore.model.enums.VoucherType;
import com.example.bookstore.repository.*;
import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class BookStoreApplicationTests {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private GenreRepository genreRepository;
	@Autowired
	private PublisherRepository publisherRepository;
	@Autowired
    private SupplierRepository supplierRepository;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private VolumeRepository volumeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private FavouriteRepository favouriteRepository;
	@Autowired
	private VoucherRepository voucherRepository;
	@Autowired
	private SeriesRepository seriesRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void save_genres() {
		Faker faker = new Faker();
		Slugify slugify = Slugify.builder().build();
		Random random = new Random();
		Set<String> generatedGenres = new HashSet<>();
		int numberOfGenres = 20;

		while (generatedGenres.size() < numberOfGenres) {
			String name = faker.book().genre();
			if (generatedGenres.add(name)) { // Add returns false if the genre already exists
				Genres genres = Genres.builder()
						.name(name)
						.type(ParentGenres.values()[random.nextInt(ParentGenres.values().length)])
						.slug(slugify.slugify(name))
						.build();
				genreRepository.save(genres);
			}
		}
	}

	@Test
	void save_publishers() {
		Faker faker = new Faker();
		Slugify slugify = Slugify.builder().build();

		for (int i = 0; i < 20; i++) {
			String name = faker.company().name();
			Publisher publisher = Publisher.builder()
					.name(name)
					.slug(slugify.slugify(name))
					.build();
			publisherRepository.save(publisher);
		}
	}

	@Test
	void save_suppliers() {
		Faker faker = new Faker();
		Slugify slugify = Slugify.builder().build();

		for (int i = 0; i < 20; i++) {
			String name = faker.company().name();
			Supplier supplier = Supplier.builder()
					.name(name)
					.slug(slugify.slugify(name))
					.build();
			supplierRepository.save(supplier);
		}
	}

	@Test
	void save_author() {
		Faker faker = new Faker();

		for (int i = 0; i < 80; i++) {
			String name = faker.book().author();
			Author author = Author.builder()
					.name(name)
					.avatar("https://i.pravatar.cc/150")
					.bio(faker.lorem().paragraph())
					.build();
			authorRepository.save(author);
		}
	}

	@Test
	void save_books() {
		Random random = new Random();
		Faker faker = new Faker();
		Slugify slugify = Slugify.builder().build();

		List<Genres> genres = genreRepository.findAll();
		List<Publisher> publishers = publisherRepository.findAll();
		List<Supplier> suppliers = supplierRepository.findAll();
		List<Author> authors = authorRepository.findAll();

		Set<String> titles = new HashSet<>();
		int falseStatusCount = 0; // Counter to track books with `status = false`
		final int MAX_FALSE_STATUS = 10; // Maximum number of books with `status = false`

		for (int i = 0; i < 100; i++) {
			String name;
			do {
				name = faker.book().title();
			} while (titles.contains(name));

			titles.add(name);

			// Select a random genre type
			ParentGenres randomType = ParentGenres.values()[random.nextInt(ParentGenres.values().length)];

			// Filter genres by the selected type
			List<Genres> filteredGenres = genres.stream()
					.filter(genre -> genre.getType() == randomType)
					.toList();

			// Random Genres
			List<Genres> rdGenres = new ArrayList<>();
			for (int j = 0; j < random.nextInt(3) + 1; j++) {
				Genres rdGenre = filteredGenres.get(random.nextInt(filteredGenres.size()));
				if (!rdGenres.contains(rdGenre)) {
					rdGenres.add(rdGenre);
				}
			}

			// Random Publisher
			Publisher rdPublisher = publishers.get(random.nextInt(publishers.size()));

			// Random Supplier
			Supplier rdSupplier = suppliers.get(random.nextInt(suppliers.size()));

			// Random Author
			Author rdAuthor = authors.get(random.nextInt(authors.size()));

			int price = ((random.nextInt(550) + 100) * 1000);
			String upperCase = String.valueOf(name.charAt(0)).toUpperCase();

			// Determine the status based on the falseStatusCount
			boolean status = falseStatusCount < MAX_FALSE_STATUS && faker.bool().bool();
			if (!status) {
				falseStatusCount++;
			}

			Books books = Books.builder()
					.title(name)
					.slug(slugify.slugify(name))
					.author(rdAuthor)
					.description(faker.lorem().paragraph())
					.cover("https://placehold.co/480x600?text=" + upperCase)
					.preview("https://placehold.co/924x612?text=" + upperCase)
					.price(price)
					.rating(faker.number().randomDouble(1, 1, 5))
					.quantity((int) faker.number().randomDouble(1, 1, 999))
					.publishYear(faker.number().numberBetween(2020, 2024))
					.type(BookType.values()[random.nextInt(BookType.values().length)])
					.genres(rdGenres)
					.publisher(rdPublisher)
					.supplier(rdSupplier)
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.status(status)
					.build();

			bookRepository.save(books);
		}
	}


	@Test
	void save_volume(){
		List<Books> books = bookRepository.findAll();

		for (Books book : books) {
			if (book.getType() == BookType.SERIES){
				for (int i = 0; i < new Random().nextInt(4) + 2; i++){
					Volumes volumes = Volumes.builder()
							.name("Volume" + " " + (i + 1))
							.displayOrder(i + 1)
							.createdAt(LocalDateTime.now())
							.updatedAt(LocalDateTime.now())
							.books(book)
							.build();
					volumeRepository.save(volumes);
				}
			} else {
				Volumes volumes = Volumes.builder()
						.name("Full Volume")
						.displayOrder(1)
						.createdAt(LocalDateTime.now())
						.updatedAt(LocalDateTime.now())
						.books(book)
						.build();
				volumeRepository.save(volumes);
			}
		}
	}

	@Test
	void save_users(){
		Faker faker = new Faker();
		Random random = new Random();

		String[] genders = {"Male", "Female"};


		for (int i = 0; i < 50; i++) {
			String name = faker.name().fullName();
			String gender = genders[random.nextInt(genders.length)];
			String upperCase = String.valueOf(name.charAt(0)).toUpperCase();

			// Encode the password "123" before saving
			String encodedPassword = passwordEncoder.encode("123");

			Users users = Users.builder()
					.userName(name)
					.email(faker.internet().emailAddress())
					.profilePicture("https://placehold.co/50x50?text=" + upperCase)
					.password(encodedPassword)
					.gender(gender)
					.dateOfBirth(faker.date().birthday())
					.enabled(true)
					.role(i == 0 || i == 1 ? UserRole.ADMIN : UserRole.USER)
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build();
			userRepository.save(users);
		}
	}

	@Test
	void save_reviews(){
		Faker faker = new Faker();
		Random random = new Random();

		List<Users> users = userRepository.findUsersByRole(UserRole.USER);
		List<Books> books = bookRepository.findAll();

		for (Books book : books) {
			for (int i = 0; i < random.nextInt(6) + 5; i++) {
				Reviews review = Reviews.builder()
						.content(faker.lorem().paragraph())
						.rating(random.nextInt(10) + 1)
						.createdAt(LocalDateTime.now())
						.updatedAt(LocalDateTime.now())
						.users(users.get(random.nextInt(users.size())))
						.books(book)
						.build();
				reviewRepository.save(review);
			}
		}
	}

	@Test
	void save_favorite(){
		// Fetch all users with the role USER
		List<Users> users = userRepository.findUsersByRole(UserRole.USER);

		// Fetch all books with status = true
		List<Books> books = bookRepository.findBooksByStatus(true);

		for (Users user : users) {
			List<Books> favoriteBooks = new ArrayList<>();

			// Randomly select up to 3 books for the user's favorites list
			for (int i = 0; i < new Random().nextInt(3) + 1; i++) {
				Books book = books.get(new Random().nextInt(books.size()));

				// Ensure the book is active and hasn't already been added
				if (book.getStatus() && !favoriteBooks.contains(book)) {
					favoriteBooks.add(book);
				}
			}

			// Save each selected book as a favorite for the user
			for (Books book : favoriteBooks) {
				Favourites favorite = Favourites.builder()
						.users(user)
						.books(book)
						.createdAt(LocalDateTime.now())
						.updatedAt(LocalDateTime.now())
						.build();
				favouriteRepository.save(favorite);
			}
		}
	}

	@Test
	void save_voucher(){
		Faker faker = new Faker();
		Random random = new Random();

		for (int i = 0; i < 100; i++) {
			String code = faker.code().ean8();  // Generate a random voucher code
			int value = random.nextInt(50) + 1;  // Generate a random value for the voucher
			VoucherType voucherType = VoucherType.values()[random.nextInt(VoucherType.values().length)];

			// Ensure FLAT_DEDUCT vouchers have values ending with "000"
			if (voucherType == VoucherType.FLAT_DEDUCTION) {
				value = (value * 1000);  // Adjust value to end in "000"
			}

			Voucher voucher = Voucher.builder()
					.code(code)
					.value(value)
					.voucherType(voucherType)
					.startDate(LocalDateTime.now())
					.endDate(LocalDateTime.now().plusMonths(1))
					.build();
			voucherRepository.save(voucher);
		}
	}

	@Test
	void save_series(){
		List<Books> booksList = bookRepository.findAll();

		for (Books book : booksList) {
			List<Volumes> volumesList = volumeRepository.findByIdOrderByDisplayOrder(book.getId());

			for (Volumes volume : volumesList) {
				// Check if the Series entry already exists
				boolean seriesExists = seriesRepository.existsByBooksAndVolumes(book, volume);

				if (!seriesExists) {
					// Create new Series entry
					Series series = Series.builder()
							.books(book)
							.volumes(volume)
							.createdAt(LocalDateTime.now())
							.updatedAt(LocalDateTime.now())
							.build();
					seriesRepository.save(series);
				}
			}
		}
	}

}
