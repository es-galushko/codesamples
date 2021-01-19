import { Platform } from 'react-native';
import { NativeEventsReceiver } from 'react-native-navigation';

import start from './src/app';

(async () => {
  try {
    if (Platform.OS === 'ios') {
      await start();
    } else {
      const appLaunched = await Navigation.isAppLaunched();

      if (appLaunched) {
        await start();
      }

      new NativeEventsReceiver().appLaunched(start);
    }
  } catch (error) {
    bugsnag.notify(error);
  }
})();
