import React from 'react';

class PageSelectionPanel extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {

    return (
      <form className="was-validated">

        <div className="mb-3">
          <label htmlFor="book">Book</label>
          <select id="book" className="custom-select" required>
            <option value="">Choose...</option>
            <option value="1">Book 1</option>
            <option value="2">Book 2</option>
            <option value="3">Book 3</option>
          </select>
          <div className="invalid-feedback">Select a Book</div>
        </div>

        <div className="mb-3">
          <label htmlFor="page">Page</label>
          <select id="page" className="custom-select" required>
            <option value="">Choose...</option>
            <option value="1">Page 1</option>
            <option value="2">Page 2</option>
            <option value="3">Page 3</option>
          </select>
          <div className="invalid-feedback">Select a Page</div>
        </div>

      </form>
    );
  }

}

export default PageSelectionPanel;
