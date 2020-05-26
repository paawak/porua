import React from 'react';

export const NEW_PAGE_OPTION = "NEW_PAGE_OPTION";

class PageSelectionPanel extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      books: [],
      pages: [],
      selectedBookId: null
    };
  }

  componentDidMount() {
    fetch("http://localhost:8080/train/book")
      .then(rawData => rawData.json())
      .then(books => this.setState({ books: books }))
      .catch(() => this.setState({ hasErrors: true }));
  }

  render() {

    const bookItems = this.state.books.map((book) =>
      <option key={book.id} value={book.id}>{book.name}</option>
    );

    const pageItems = this.state.pages.map((page) =>
      <option key={page.id} value={page.id}>{page.name}</option>
    );

    return (
      <form className="was-validated">

        <div className="mb-3">
          <label htmlFor="book">Book</label>
          <select id="book" className="custom-select" required
          onChange={e => {
              let bookId = e.target.value;
              this.setState({
                pages: [],
                selectedBookId: bookId
              });

              if (bookId !== '') {
                fetch("http://localhost:8080/train/page?bookId=" + bookId)
                  .then(rawData => rawData.json())
                  .then(pages => this.setState({ pages: pages }))
                  .catch(() => this.setState({ hasErrors: true }));
              }

            }
          }
          >
            <option value="">Choose...</option>
            {bookItems}
          </select>
          <div className="invalid-feedback">* Select a Book</div>
        </div>

        <div className="mb-3">
          <label htmlFor="page">Page</label>
          <select id="page" className="custom-select" required>
            <option value="">Choose...</option>
            <option value={NEW_PAGE_OPTION}>Add New Page</option>
            {pageItems}
          </select>
          <div className="invalid-feedback">* Select a Page</div>
        </div>

        <button type="button" className="btn btn-success">Submit</button>

      </form>
    );
  }

}

export default PageSelectionPanel;
