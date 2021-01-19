import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import { ScheduleInfo, UnitType } from 'configs/propTypes';
import { requestSchedulesList, requestCurrentUnit } from 'redux/schedule';

import Subheader from 'components/Subheader';
import EmptyList from 'components/EmptyList';

import AddScheduleModal from './AddScheduleModal';

import { Container, IconAdd, IconList, List, ListItem } from './styled';

class SchedulesList extends Component {
  static propTypes = {
    history: PropTypes.object.isRequired,
    match: PropTypes.object.isRequired,
    location: PropTypes.object.isRequired,
    requestSchedulesList: PropTypes.func.isRequired,
    requestCurrentUnit: PropTypes.func.isRequired,
    list: PropTypes.arrayOf(ScheduleInfo).isRequired,
    currentUnit: UnitType.isRequired,
  }

  state = {
    isModalVisible: false,
  }

  componentDidMount() {
    const unitId = this.props.match.params.unitId;
    this.props.requestSchedulesList({ unitId });
    this.props.requestCurrentUnit({ unitId });
  }

  onListClick = () => {
    this.props.history.push('/scheduler');
  }

  openModal = () => {
    this.setState({ isModalVisible: true });
  }

  closeModal = () => {
    this.setState({ isModalVisible: false });
  }

  toSchedulesDetails = (id) => {
    this.props.history.push(`${this.props.location.pathname}/details/${id}`);
  }

  render() {
    const { list, currentUnit } = this.props;

    return (
      <Container>
        <Subheader
          title={currentUnit.Name}
          zIndex={5}
          leftButtons={[
            {
              icon: <IconList />,
              handler: this.onListClick,
              hint: 'Back',
            },
          ]}
        />
        <Subheader
          title="All Schedules"
          zIndex={4}
          rightButtons={[{
            icon: <IconAdd />,
            handler: this.openModal,
            hint: 'Add',
          }]}
          showSite={false}
        />
        {list && list.length ?
          <List>
            {list.map(item =>
              <ListItem key={item.Id} onClick={() => this.toSchedulesDetails(item.Id)} primaryText={item.Name} />)
            }
          </List>
          : <EmptyList text="There are no schedules" />
        }
        <AddScheduleModal
          open={this.state.isModalVisible}
          handleClose={this.closeModal}
        />
      </Container>
    );
  }
}

const mapStateToProps = ({ schedule }) => ({
  list: schedule.scheluesList,
  currentUnit: schedule.currentUnit,
});

export default connect(mapStateToProps, { requestSchedulesList, requestCurrentUnit })(SchedulesList);
