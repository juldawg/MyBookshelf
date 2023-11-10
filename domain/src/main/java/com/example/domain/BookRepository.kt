package com.example.domain

class BookRepository {
    val books = listOf(
        Book("Capital et Idéologie", "Thomas Piketty", 3, "Plutôt cool"),
        Book("Le Pouvoir Rhétorique", "Clément Viktorovich", 2, "Un peu naze"),
        Book("Bureaucratie", "David Graeber", 5, "Un truc de ouf !"),
        Book("Bullshit Job", "David Graeber", 5, "Super super cooool")
    ).sortedBy(Book::title)
}