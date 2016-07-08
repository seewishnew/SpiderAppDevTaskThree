        package com.example.vishnu.spidertaskthree;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;
        import com.j256.ormlite.field.DatabaseField;
        import com.j256.ormlite.table.DatabaseTable;

        import org.apache.commons.lang.builder.EqualsBuilder;
        import org.apache.commons.lang.builder.HashCodeBuilder;
        import org.apache.commons.lang.builder.ToStringBuilder;

public class Movie {

    @DatabaseField(generatedId = true)
    private long id = 0;

    @DatabaseField
    @SerializedName("Title")
    @Expose
    private String title;

    @DatabaseField
    @SerializedName("Year")
    @Expose
    private String year;

    @DatabaseField
    @SerializedName("Rated")
    @Expose
    private String rated;

    @DatabaseField
    @SerializedName("Released")
    @Expose
    private String released;

    @DatabaseField
    @SerializedName("Runtime")
    @Expose
    private String runtime;

    @DatabaseField
    @SerializedName("Genre")
    @Expose
    private String genre;

    @DatabaseField
    @SerializedName("Director")
    @Expose
    private String director;

    @DatabaseField
    @SerializedName("Writer")
    @Expose
    private String writer;

    @DatabaseField
    @SerializedName("Actors")
    @Expose
    private String actors;

    @DatabaseField
    @SerializedName("Plot")
    @Expose
    private String plot;

    @DatabaseField
    @SerializedName("Language")
    @Expose
    private String language;

    @DatabaseField
    @SerializedName("Country")
    @Expose
    private String country;

    @DatabaseField
    @SerializedName("Awards")
    @Expose
    private String awards;

    @DatabaseField
    @SerializedName("Poster")
    @Expose
    private String poster;

    @DatabaseField
    @SerializedName("Metascore")
    @Expose
    private String metascore;

    @DatabaseField
    @SerializedName("imdbRating")
    @Expose
    private String imdbRating;

    @DatabaseField
    @SerializedName("imdbVotes")
    @Expose
    private String imdbVotes;

    @DatabaseField
    @SerializedName("imdbID")
    @Expose
    private String imdbID;

    @DatabaseField
    @SerializedName("Type")
    @Expose
    private String type;

    @DatabaseField
    @SerializedName("Response")
    @Expose
    private String response;

    /**
     * No args constructor for use in serialization
     *
     */
    public Movie() {
    }

    /**
     *
     * @param genre
     * @param metascore
     * @param imdbVotes
     * @param runtime
     * @param imdbID
     * @param type
     * @param director
     * @param plot
     * @param country
     * @param response
     * @param released
     * @param imdbRating
     * @param title
     * @param awards
     * @param poster
     * @param year
     * @param actors
     * @param language
     * @param rated
     * @param writer
     */
    public Movie(String title, String year, String rated, String released, String runtime, String genre, String director, String writer, String actors, String plot, String language, String country, String awards, String poster, String metascore, String imdbRating, String imdbVotes, String imdbID, String type, String response) {
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.director = director;
        this.writer = writer;
        this.actors = actors;
        this.plot = plot;
        this.language = language;
        this.country = country;
        this.awards = awards;
        this.poster = poster;
        this.metascore = metascore;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.imdbID = imdbID;
        this.type = type;
        this.response = response;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The year
     */
    public String getYear() {
        return year;
    }

    /**
     *
     * @param year
     * The Year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     *
     * @return
     * The rated
     */
    public String getRated() {
        return rated;
    }

    /**
     *
     * @param rated
     * The Rated
     */
    public void setRated(String rated) {
        this.rated = rated;
    }

    /**
     *
     * @return
     * The released
     */
    public String getReleased() {
        return released;
    }

    /**
     *
     * @param released
     * The Released
     */
    public void setReleased(String released) {
        this.released = released;
    }

    /**
     *
     * @return
     * The runtime
     */
    public String getRuntime() {
        return runtime;
    }

    /**
     *
     * @param runtime
     * The Runtime
     */
    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    /**
     *
     * @return
     * The genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     *
     * @param genre
     * The Genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     *
     * @return
     * The director
     */
    public String getDirector() {
        return director;
    }

    /**
     *
     * @param director
     * The Director
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     *
     * @return
     * The writer
     */
    public String getWriter() {
        return writer;
    }

    /**
     *
     * @param writer
     * The Writer
     */
    public void setWriter(String writer) {
        this.writer = writer;
    }

    /**
     *
     * @return
     * The actors
     */
    public String getActors() {
        return actors;
    }

    /**
     *
     * @param actors
     * The Actors
     */
    public void setActors(String actors) {
        this.actors = actors;
    }

    /**
     *
     * @return
     * The plot
     */
    public String getPlot() {
        return plot;
    }

    /**
     *
     * @param plot
     * The Plot
     */
    public void setPlot(String plot) {
        this.plot = plot;
    }

    /**
     *
     * @return
     * The language
     */
    public String getLanguage() {
        return language;
    }

    /**
     *
     * @param language
     * The Language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     *
     * @return
     * The country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     * The Country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     * The awards
     */
    public String getAwards() {
        return awards;
    }

    /**
     *
     * @param awards
     * The Awards
     */
    public void setAwards(String awards) {
        this.awards = awards;
    }

    /**
     *
     * @return
     * The poster
     */
    public String getPoster() {
        return poster;
    }

    /**
     *
     * @param poster
     * The Poster
     */
    public void setPoster(String poster) {
        this.poster = poster;
    }

    /**
     *
     * @return
     * The metascore
     */
    public String getMetascore() {
        return metascore;
    }

    /**
     *
     * @param metascore
     * The Metascore
     */
    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    /**
     *
     * @return
     * The imdbRating
     */
    public String getImdbRating() {
        return imdbRating;
    }

    /**
     *
     * @param imdbRating
     * The imdbRating
     */
    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    /**
     *
     * @return
     * The imdbVotes
     */
    public String getImdbVotes() {
        return imdbVotes;
    }

    /**
     *
     * @param imdbVotes
     * The imdbVotes
     */
    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    /**
     *
     * @return
     * The imdbID
     */
    public String getImdbID() {
        return imdbID;
    }

    /**
     *
     * @param imdbID
     * The imdbID
     */
    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The Type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The response
     */
    public String getResponse() {
        return response;
    }

    /**
     *
     * @param response
     * The Response
     */
    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(title).append(year).append(rated).append(released).append(runtime).append(genre).append(director).append(writer).append(actors).append(plot).append(language).append(country).append(awards).append(poster).append(metascore).append(imdbRating).append(imdbVotes).append(imdbID).append(type).append(response).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Movie) == false) {
            return false;
        }
        Movie rhs = ((Movie) other);
        return new EqualsBuilder().append(title, rhs.title).append(year, rhs.year).append(rated, rhs.rated).append(released, rhs.released).append(runtime, rhs.runtime).append(genre, rhs.genre).append(director, rhs.director).append(writer, rhs.writer).append(actors, rhs.actors).append(plot, rhs.plot).append(language, rhs.language).append(country, rhs.country).append(awards, rhs.awards).append(poster, rhs.poster).append(metascore, rhs.metascore).append(imdbRating, rhs.imdbRating).append(imdbVotes, rhs.imdbVotes).append(imdbID, rhs.imdbID).append(type, rhs.type).append(response, rhs.response).isEquals();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

