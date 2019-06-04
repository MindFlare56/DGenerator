package mindf.utils

class Regex {

    companion object {
        fun isSingleNameValid(firstName: String, pattern: String = "[A-Z]{1}[a-zA-Z]{1,79}"): Boolean {
            return Regex(pattern = pattern).containsMatchIn(input = firstName)
        }

        fun isTextValid(text: String, range: Int = 80, pattern: String = "[A-Z]{1}[a-zA-Z]{1,$range}"): Boolean {
            return Regex(pattern = pattern).containsMatchIn(input = text)
        }

        fun isPhoneNumberValid(phoneNumber: String, pattern: String = "((1-)|(1 )|(1))?\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})") : Boolean {
            return Regex(pattern = pattern).containsMatchIn(input = phoneNumber)
        }

        fun isDoubleValid(salary: String, pattern: String = "[0-9]+(\\.[0-9]+)?") : Boolean {
            return Regex(pattern = pattern).containsMatchIn(input = salary)
        }

        fun isBirthDateValid(birthDate: String, pattern: String = "(\\d{4})-(\\d{2})-(\\d{2})") : Boolean { //todo validate age is over 0 and under 120
            return Regex(pattern = pattern).containsMatchIn(input = birthDate)
        }

        fun isDateValid(birthDate: String, pattern: String = "(\\d{4})-(\\d{2})-(\\d{2})"): Boolean {
            return Regex(pattern = pattern).containsMatchIn(input = birthDate)
        }
    }
}