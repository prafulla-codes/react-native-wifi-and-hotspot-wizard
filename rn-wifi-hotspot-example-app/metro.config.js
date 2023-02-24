/**
 * Metro configuration for React Native
 * https://github.com/facebook/react-native
 *
 * @format
 */

var packagePath = 'C:\\Projects\\react-native-wifi-and-hotspot-wizard\\library\\'
module.exports = {
  watchFolders:[packagePath],
  resolver: {
    "react-native-wifi-and-hotspot-wizard":packagePath
  },
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: false,
      },
    }),
  },
};
