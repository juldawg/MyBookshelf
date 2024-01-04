package com.viseo.mybookshelf.ui.booksearch

import com.viseo.mybookshelf.domain.Book
import com.viseo.mybookshelf.domain.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class BookRepositoryMock : BookRepository() {
    override fun getBooks(): Flow<List<Book>> = flowOf(
        listOf(
            Book(
                isbn10 = "0123456789",
                isbn13 = "9780123456789",
                title = "The Catcher in the Rye",
                authors = listOf("J.D. Salinger"),
                publishers = listOf("Little, Brown and Company"),
                cover = "https://example.com/catcher-in-the-rye.jpg",
                numberOfPages = 224,
                rating = 4,
                notes = "Classic coming-of-age novel."
            ),
            Book(
                isbn10 = "9876543210",
                isbn13 = "9789876543210",
                title = "To Kill a Mockingbird",
                authors = listOf("Harper Lee"),
                publishers = listOf("J.B. Lippincott & Co."),
                cover = "https://example.com/to-kill-a-mockingbird.jpg",
                numberOfPages = 281,
                rating = 5,
                notes = "Powerful exploration of racial injustice."
            ),
            Book(
                isbn10 = "0123456781",
                isbn13 = "9780123456781",
                title = "1984",
                authors = listOf("George Orwell"),
                publishers = listOf("Secker & Warburg"),
                cover = "https://example.com/1984.jpg",
                numberOfPages = 328,
                rating = 5,
                notes = "Dystopian masterpiece."
            ),
            Book(
                isbn10 = "9876543211",
                isbn13 = "9789876543211",
                title = "The Great Gatsby",
                authors = listOf("F. Scott Fitzgerald"),
                publishers = listOf("Charles Scribner's Sons"),
                cover = "https://example.com/the-great-gatsby.jpg",
                numberOfPages = 180,
                rating = 4,
                notes = "Classic American novel."
            ),
            Book(
                isbn10 = "0123456782",
                isbn13 = "9780123456782",
                title = "Pride and Prejudice",
                authors = listOf("Jane Austen"),
                publishers = listOf("T. Egerton, Whitehall"),
                cover = "https://example.com/pride-and-prejudice.jpg",
                numberOfPages = 279,
                rating = 5,
                notes = "Romantic novel of manners."
            ),
            Book(
                isbn10 = "9876543212",
                isbn13 = "9789876543212",
                title = "One Hundred Years of Solitude",
                authors = listOf("Gabriel García Márquez"),
                publishers = listOf("Harper & Row"),
                cover = "https://example.com/one-hundred-years-of-solitude.jpg",
                numberOfPages = 417,
                rating = 5,
                notes = "Magical realism at its finest."
            )
        ).sortedBy(Book::title)
    )

    override fun getBook(title: String): Flow<Book?> {
        TODO("Not yet implemented")
    }

    override fun insertBooks(books: List<Book>) {
        TODO("Not yet implemented")
    }

    override fun updateBook(book: Book) {
        TODO("Not yet implemented")
    }

    override fun searchBook(key: String): List<Book> = listOf(
        Book(
            isbn10 = "1234567890",
            isbn13 = "9781234567890",
            title = "Sample Book",
            authors = listOf("Author 1", "Author 2"),
            publishers = listOf("Publisher 1", "Publisher 2"),
            cover = "https://example.com/cover.jpg",
            numberOfPages = 200,
            rating = 4,
            notes = "This is a sample book for testing purposes."
        ),
        Book(
            isbn10 = "1234567890",
            isbn13 = "9781234567890",
            title = "Sample Book2",
            authors = listOf("Author 1", "Author 2"),
            publishers = listOf("Publisher 1", "Publisher 2"),
            cover = "https://example.com/cover.jpg",
            numberOfPages = 200,
            rating = 4,
            notes = "This is a sample book for testing purposes."
        )
    )
}