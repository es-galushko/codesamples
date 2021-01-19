import 'core-js/es6/symbol';
import 'core-js/fn/symbol/iterator';

import { Navigation } from 'react-native-navigation';

import '@configs/ReactotronConfig';
import { version } from '../package.json';

import { initialState as authInitialState } from '@redux/modules/auth';

import http from '@services/http';
import bugsnag from '@services/bugsnag';

import CookieStorage from '@services/storage/cookie';
import VersionStorage from '@services/storage/version';
import SelectedServerStorage from '@services/storage/selectedServer';
import SelectedSiteStorage from '@services/storage/selectedSite';

import registerScreens from './screens';
import CombinedProvider from './CombinedProvider';
import configureStore from './redux/configureStore';

const start = async () => {
  const [cookie, selectedServer, selectedSite] = await Promise.all([
    CookieStorage.get(),
    SelectedServerStorage.get(),
    SelectedSiteStorage.get(),
  ]);

  await VersionStorage.save(version);

  const persistedState = {
    auth: {
      ...authInitialState,
      cookie: cookie || '',
      selectedServer: selectedServer || '',
      selectedSite: selectedSite || {},
    },
  };

  const store = configureStore(persistedState);

  registerScreens(store, CombinedProvider);

  http.defaults.baseURL = selectedServer;

  let screen = '';

  if (selectedServer) {
    screen = cookie ? 'Home' : 'AuthLogin';
  } else {
    screen = 'AuthServer';
  }

  Navigation.startSingleScreenApp({
    screen: {
      screen,
    },
    appStyle: {
      orientation: 'portrait',
      screenBackgroundColor: '#ffcd36',
    },
  });
};
