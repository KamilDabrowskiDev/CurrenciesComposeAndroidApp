# PLN Exchange Rates Viewer

An application for displaying current and historical
exchange rate values of PLN currency, using data from
the [NBP API](https://api.nbp.pl/).

## 🌟 Features

- **Main Screen: Current Exchange Rates**
  - Displays a list of average exchange rates for currencies
    from **Table A** and **Table B** of the NBP API.

- **Currency Details Screen: Historical Trends**
  - View historical exchange rates of a selected currency
    for the **last 2 weeks**.
  - Historical exchange rates are sorted in descending order by date.
  - Significant rate changes (±10%) are automatically
    **highlighted**.

- **Data layer unit tested**
- 
- **Logic layer unit tested**

## 🛠️ Technology Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** MVI
- **Async:** Coroutines and Flows
- **Dependency Injection:** Hilt
- **Network:** Retrofit, OkHTTP, Moshi
- **Unit Tests:** Junit, JunitParams, Robolectric, Mockk  