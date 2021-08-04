package it.unibo.parkservicestatus

class StateAdapter {
    companion object {
        fun convertToJSON(state: String): String {
            var stateJSON = ""

            var content = state.split("sonar:", "weight:", "thermometer:", "trolley:", "fan:")

            stateJSON = "{ \"sonar\": \"" + content.elementAt(1) + "\"," +
                    "\"weight\": \"" + content.elementAt(2) + "\"," +
                    "\"thermometer\": \"" + content.elementAt(3) + "\"," +
                    "\"trolley\": \"" + content.elementAt(4) + "\"," +
                    "\"fan\": \"" + content.elementAt(5) + "\"" +
                    " }"

            return stateJSON
        }
    }
}