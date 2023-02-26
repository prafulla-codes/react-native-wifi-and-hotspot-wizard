import {StyleSheet} from 'react-native';

let style = StyleSheet.create({
    header:{
        backgroundColor:'#212121',
        flexDirection:'row',
        elevation:0.5,
        alignItems:'flex-end'
    },
    headerText:{
        color:"#fff",
        fontSize:18,
        fontWeight:'bold'
    },
    text:{
        fontSize:16,
        fontWeight:'bold',
        color:'black',
        marginBottom:2,
    },
    buttonText:{
        fontSize:18,
        fontWeight:'bold',
        marginBottom:2,
    },
    textInput:{
        borderBottomColor:"green",
        borderBottomWidth:2,
        color:'black'
    }
})

export default style;