import java.time.Year

data class Book(val name: String, val genre: Genre, val year: Year, val authors: List<Author>)

data class Author(val firstName: String, val lastName: String)

data class User(val firstName: String, val lastName: String) {
    val books: MutableList<Book> = mutableListOf()
}

enum class Genre {
    Fantastic,
    Philosophy,
    Novel
}

sealed class Status {
    object Available : Status()
    data class UsedBy(val user: User) : Status()
    object ComingSoon : Status()
    object Restoration : Status()
}

interface LibraryService {

    fun findBooks(substring: String?, genre: Genre?, year: Year?, author: Author?): List<Book>

    fun getAllBooks(): List<Book>
    fun getAllAvailableBooks(): List<Book>

    fun getBookStatus(book: Book): Status?
    fun getAllBookStatuses(): Map<Book, Status>

    fun setBookStatus(book: Book, status: Status)

    fun addBook(book: Book, status: Status = Status.Available)

    fun registerUser(firstName: String, lastName: String)
    fun unregisterUser(user: User)

    fun takeBook(user: User, book: Book)
    fun returnBook(book: Book)
}

class Library : LibraryService {

    private var books: MutableMap<Book, Status> = mutableMapOf()
    private var users: ArrayList<User> = arrayListOf()

    override fun getAllBooks(): List<Book> = books.keys.toList()

    override fun getAllAvailableBooks(): List<Book> =
        books.filter { book -> book.value == Status.Available }.map { book -> book.key }

    override fun getBookStatus(book: Book): Status? = books[book]

    override fun getAllBookStatuses(): Map<Book, Status> = books

    override fun setBookStatus(book: Book, status: Status) {
        if (!existBook(book))
            addBook(book)
        books[book] = status
    }

    override fun addBook(book: Book, status: Status) {
        books[book] = status
    }

    override fun registerUser(firstName: String, lastName: String) {
        val user = User(firstName, lastName)
        if (!isUserRegistered(user))
            users.add(user)
    }

    override fun unregisterUser(user: User) {
        if (isUserRegistered(user))
            users.remove(user)
    }

    override fun takeBook(user: User, book: Book) {
        if (isUserRegistered(user) && user.books.size < 3 && existBook(book) && books[book] == Status.Available) {
            books[book] = Status.UsedBy(user)
            user.books.add(book)
        }
    }

    override fun returnBook(book: Book) {
        val user = users.find { user -> user.books.contains(book) } ?: return
        books[book] = Status.Available
        user.books.remove(book)
    }

    override fun findBooks(substring: String?, genre: Genre?, year: Year?, author: Author?): List<Book> {
        var books = setOf<Book>()
        if (substring != null)
            books = books.union(findBooks(substring))
        if (genre != null)
            books = books.union(findBooks(genre))
        if (year != null)
            books = books.union(findBooks(year))
        if (author != null)
            books = books.union(findBooks(author))
        return books.toList()
    }

    private fun findBooks(substring: String): List<Book> =
        books.filter { book -> book.key.name == substring }.map { book -> book.key }

    private fun findBooks(author: Author): List<Book> =
        books.filter { book -> book.key.authors.contains(author) }.map { book -> book.key }

    private fun findBooks(year: Year): List<Book> =
        books.filter { book -> book.key.year == year }.map { book -> book.key }

    private fun findBooks(genre: Genre): List<Book> =
        books.filter { book -> book.key.genre == genre }.map { book -> book.key }

    private fun isUserRegistered(user: User): Boolean = users.contains(user)

    private fun existBook(book: Book): Boolean = books.contains(book)
}

fun main() {
    val library: LibraryService = Library()

    val books = listOf(
        Book("Рассуждение о методе", Genre.Philosophy, Year.now(), listOf(Author("Рене", "Декард"))),
        Book("Охота на овец", Genre.Fantastic, Year.now(), listOf(Author("Харуки", "Мураками"))),
        Book("12 стульев", Genre.Novel, Year.now(), listOf(Author("Ильф", ""), Author("Петров", ""))),
        Book("Алхимик", Genre.Philosophy, Year.now(), listOf(Author("Пауло", "Коэльо")))
    )

    val users = listOf(User("Саша", ",Белый"), User("Космос", ""), User("Пчёла", ""), User("Фил", ""))

    library.setBookStatus(books[0], Status.ComingSoon)

    library.registerUser(users[0].firstName, users[0].lastName)

    library.takeBook(users[0], books[0])

    println(library.getBookStatus(books[0])) //1

    library.addBook(books[0])
    library.addBook(books[1])
    library.addBook(books[2])
    library.addBook(books[3])

    library.takeBook(users[0], books[0])

    println(library.getBookStatus(books[0])) //2

    library.takeBook(users[0], books[1])
    library.takeBook(users[0], books[2])
    library.takeBook(users[0], books[3])

    println(library.getBookStatus(books[2])) //3

    println(library.getBookStatus(books[3])) //4

    library.takeBook(users[1], books[3])

    println(library.getBookStatus(books[3])) //5

    library.registerUser(users[1].firstName, users[1].lastName)

    library.takeBook(users[1], books[3])

    println(library.getBookStatus(books[3])) //6
}










