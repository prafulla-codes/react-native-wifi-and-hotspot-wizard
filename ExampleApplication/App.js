/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  Image,
  StatusBar,
} from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import Home from './pages/Home';
import style from './assets/styles/style';

const Stack = createStackNavigator();

const App = () => {
  StatusBar.setBackgroundColor("#212121")

  return (
    <NavigationContainer initialRouteName="Home">
      <Stack.Navigator>
        <Stack.Screen name="Home"  options={{
          headerTitleAlign:"left",
          headerTitle:props=>(
            <View style={style.header}>
              <Image source={require("./assets/images/logo.png")} style={{width:25,height:25}} ></Image>
              <Text style={style.headerText}> React Native Wifi & Hotspot Wizard Example </Text> 
            </View>
          ),
          headerStyle: {
            backgroundColor: '#212121',
          },
          headerTintColor: '#fff',
          headerTitleStyle: {
            fontWeight: 'bold',
          },
        }}  component={Home} />
      </Stack.Navigator>
    </NavigationContainer>
  );

};



export default App;
