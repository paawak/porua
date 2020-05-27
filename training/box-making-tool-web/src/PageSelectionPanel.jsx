import React from 'react';

export const NEW_PAGE_OPTION = "NEW_PAGE_OPTION";

class PageSelectionPanel extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      books: [],
      pages: [],
      selectedBookId: null,
      selectedPageId: null
    };
    this.handleButtonClick = this.handleButtonClick.bind(this);
  }

  handleButtonClick() {
    if ((this.state.selectedBookId == null) || (this.state.selectedBookId === '')) {
      alert('Select a Book');
      return;
    }

    if ((this.state.selectedPageId == null) || (this.state.selectedPageId === '')) {
      alert('Select a Page');
      return;
    }

    if (this.state.selectedPageId === NEW_PAGE_OPTION) {
      let bookId = this.state.selectedBookId;
      let book = this.state.books.filter(book => book.id === parseInt(bookId, 10))[0];
      this.props.showNewPagePanel(book);
    } else {
      fetch("http://localhost:8080/train/word?bookId=" + this.state.selectedBookId + "&pageImageId=" + this.state.selectedPageId)
        .then(rawData => rawData.json())
        .then(data => this.props.ocrWordsRecievedForExistingPage(data))
        .catch(() => this.setState({ hasErrors: true }));
    }
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
              selectedBookId: bookId,
              selectedPageId: null,
              pages: []
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
          <select id="page" className="custom-select" required
            onChange={e => {
                this.setState({
                  selectedPageId: e.target.value
                });
              }
            }
          >
            <option value="">Choose...</option>
            <option value={NEW_PAGE_OPTION}>Add New Page</option>
            {pageItems}
          </select>
          <div className="invalid-feedback">* Select a Page</div>
        </div>

        <button type="button" className="btn btn-success" onClick={this.handleButtonClick}>Submit</button>

      </form>
    );
  }

}

export default PageSelectionPanel;
