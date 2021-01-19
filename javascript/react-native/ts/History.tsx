import React from 'react';
import { useTranslation } from 'react-i18next';
import { useDispatch, useSelector } from 'react-redux';

import * as Alert from '@/utils/alert';
import { actions } from '@/redux/counters';
import { HistoryScreenProps } from '@/navigator';
import { getHistoryByCounterId, getCounterById } from '@/redux/counters/selectors';

import SecondaryButton from '@/components/buttons/Secondary';
import BottomControls from '@/components/BottomControls';

import * as S from './styled';
import List from './List';
import EmptyPlaceholder from './EmptyPlaceholder';

const History = ({ navigation, route }: HistoryScreenProps) => {
  const { id } = route.params;
  const counter = useSelector(getCounterById(id));
  const history = useSelector(getHistoryByCounterId(id));
  const dispatch = useDispatch();
  const { t } = useTranslation(['history', 'alert']);

  const handleBackPress = () => navigation.navigate('Counter', { id });

  const handleClearHistoryPress = () => {
    Alert.clearHistory(t, () => dispatch(actions.clearHistory(id)), 'alert:clearHistory.');
  };

  const isHistoryEmpty = !history.length;

  return (
    <S.Container>
      {!isHistoryEmpty ? <List data={history} /> : <EmptyPlaceholder />}
      <BottomControls
        backTitle={counter.title}
        onBack={handleBackPress}
        rightButton={
          !isHistoryEmpty ? (
            <SecondaryButton title={t('buttons.clearHistory')} onPress={handleClearHistoryPress} />
          ) : null
        }
      />
    </S.Container>
  );
};

export default History;
