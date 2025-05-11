/*
   Copyright 2022-2025 Michael Strasser.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package mjs.kotest

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.random.Random

fun randomWord(): String = words.random()

fun randomName(maxWords: Int = 10): String = randomWord().replaceFirstChar { it.uppercase() } + " " +
    Array(Random.nextInt(maxWords)) { randomWord() }.toList().joinToString(" ")

fun passingTest() {
    val word = randomWord()
    word shouldBe word
}

fun failingTest() {
    val word = randomWord()
    word shouldNotBe word
}

fun errorTest() {
    throw RuntimeException("Oh noes")
}

private val words = listOf(
    "April", "August", "December", "Earth", "February", "Friday", "God", "I", "January", "July", "June", "March",
    "May", "Monday", "November", "October", "Saturday", "September", "Sunday", "Thursday", "Tuesday", "Wednesday",
    "actor", "adjective", "adult", "afternoon", "air", "airport", "alive", "animal", "apartment", "apple", "arm",
    "army", "art", "artist", "attack", "author", "baby", "back", "bad", "bag", "ball", "banana", "band", "bank",
    "bar", "bathroom", "beach", "beard", "beat", "beautiful", "bed", "bedroom", "beef", "beer", "bend", "beverage",
    "bicycle", "big", "bill", "billion", "bird", "black", "blind", "blood", "blue", "boat", "body", "bone", "book",
    "bottle", "bottom", "box", "boy", "brain", "bread", "break", "breakfast", "bridge", "brother", "brown", "build",
    "building", "burn", "bus", "buy", "cake", "call", "camera", "camp", "car", "card", "carry", "cat", "catch",
    "ceiling", "cell", "centimeter", "chair", "cheap", "cheese", "chicken", "child", "church", "circle", "city",
    "clay", "clean", "clock", "close", "clothing", "club", "coat", "coffee", "cold", "color", "computer",
    "consonant", "contract", "cook", "cool", "copper", "corn", "corner", "count", "country", "court", "cow",
    "crowd", "cry", "cup", "curved", "cut", "dance", "dark", "date", "daughter", "day", "dead", "deaf", "death",
    "deep", "diamond", "die", "dig", "dinner", "direction", "dirty", "disease", "doctor", "dog", "dollar", "door",
    "dot", "down", "draw", "dream", "dress", "drink", "drive", "drug", "dry", "dust", "ear", "earth", "east", "eat",
    "edge", "egg", "eight", "eighteen", "eighty", "election", "electronics", "eleven", "energy", "engine", "evening",
    "exercise", "expensive", "explode", "eye", "face", "fall", "family", "famous", "fan", "farm", "fast", "father",
    "feed", "female", "fifteen", "fifth", "fifty", "fight", "find", "finger", "fire", "first", "fish", "five",
    "flat", "floor", "flower", "fly", "follow", "food", "foot", "forest", "fork", "forty", "four", "fourteen",
    "fourth", "friend", "front", "game", "garden", "gasoline", "gift", "girl", "glass", "go", "gold", "good",
    "grandfather", "grandmother", "grass", "gray", "green", "ground", "grow", "gun", "hair", "half", "hand",
    "hang", "happy", "hard", "hat", "he", "head", "healthy", "hear", "heart", "heat", "heaven", "heavy", "hell",
    "high", "hill", "hole", "horse", "hospital", "hot", "hotel", "hour", "house", "human", "hundred", "husband",
    "ice", "image", "inch", "injury", "inside", "instrument", "island", "it", "job", "juice", "jump", "key",
    "kill", "kilogram", "king", "kiss", "kitchen", "knee", "knife", "lake", "lamp", "laptop", "large", "laugh",
    "lawyer", "leaf", "learn", "left", "leg", "lemon", "letter", "library", "lie", "lift", "light", "lip",
    "listen", "little", "location", "lock", "long", "loose", "lose", "loud", "love", "low", "lunch", "magazine",
    "male", "man", "manager", "map", "market", "marriage", "marry", "material", "mean", "medicine", "melt", "metal",
    "meter", "milk", "million", "minute", "mix", "money", "month", "moon", "morning", "mother", "mountain", "mouse",
    "mouth", "movie", "murder", "music", "narrow", "nature", "neck", "needle", "neighbor", "network", "new",
    "newspaper", "nice", "night", "nine", "nineteen", "ninety", "no", "north", "nose", "note", "nuclear", "number",
    "ocean", "office", "oil", "old", "one", "open", "orange", "outside", "page", "pain", "paint", "pants", "paper",
    "parent", "park", "pass", "patient", "pattern", "pay", "peace", "pen", "pencil", "person", "phone",
    "photograph", "piece", "pig", "pink", "plane", "plant", "plastic", "plate", "play", "player", "plural",
    "pocket", "poison", "police", "pool", "poor", "pork", "pound", "pray", "president", "price", "priest", "prison",
    "program", "pull", "push", "queen", "quiet", "race", "radio", "rain", "red", "religion", "reporter",
    "restaurant", "rice", "rich", "right", "ring", "river", "road", "roof", "room", "root", "run", "sad", "salt",
    "sand", "say", "school", "science", "screen", "sea", "season", "second", "secretary", "see", "seed", "sell",
    "seven", "seventeen", "seventy", "sex", "shake", "shallow", "she", "ship", "shirt", "shoes", "shoot", "shop",
    "short", "shoulder", "sick", "side", "sign", "silver", "sing", "singular", "sister", "sit", "six", "sixteen",
    "sixty", "skin", "skirt", "sky", "sleep", "slow", "small", "smell", "smile", "snow", "soap", "soft", "soil",
    "soldier", "son", "song", "sound", "soup", "south", "space", "speak", "spoon", "sport", "spring", "square",
    "stain", "stand", "star", "station", "stir", "stone", "stop", "store", "straight", "street", "strong",
    "student", "sugar", "suit", "summer", "sun", "sweat", "swim", "table", "tall", "taste", "tea", "teach",
    "teacher", "team", "tear", "technology", "telephone", "television", "temperature", "ten", "theater", "they",
    "thick", "thin", "think", "third", "thirteen", "thirty", "thousand", "three", "throw", "ticket", "tight",
    "time", "tire", "toe", "tongue", "tool", "tooth", "top", "touch", "town", "train", "transportation", "tree",
    "truck", "turn", "twelve", "twenty", "two", "ugly", "university", "up", "valley", "verb", "victim", "voice",
    "vowel", "waiter", "wake", "walk", "wall", "war", "warm", "wash", "watch", "water", "wave", "we", "weak", "wear",
    "wedding", "week", "weight", "west", "wet", "white", "wide", "wife", "win", "wind", "window", "wine", "wing",
    "winter", "woman", "wood", "work", "world", "write", "yard", "year", "yellow", "yes", "you", "young", "zero",
)
