package com.ir.voicespellchecker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech tts;
    private ShapeableImageView micView;
    private MaterialButton resetButton, nextButton, previousButton, repeatButton, giveUpButton;
    private AutoCompleteTextView rowView;
    private TextInputEditText spokenWordView;
    private TextInputLayout spokenWordLayout;
    private ConstraintLayout constraintLayout;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private ArrayAdapter<String> rowAdapter;
    private ArrayList<String> rows, row1, row2, row3, row4, row5, row6, row7, row8;
    private ArrayList<ArrayList<String>> words;
    private String spokenWord;
    private int currIndex = -1;
    private int rowNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        micView = findViewById(R.id.mic);
        spokenWordView = findViewById(R.id.spokenWord);
        constraintLayout = findViewById(R.id.constraintLayout);
        resetButton = findViewById(R.id.reset);
        nextButton = findViewById(R.id.next);
        previousButton = findViewById(R.id.prev);
        repeatButton = findViewById(R.id.repeat);
        giveUpButton = findViewById(R.id.giveup);
        rowView = findViewById(R.id.row);
        spokenWordLayout = findViewById(R.id.spokenWordLayout);
        row1 = new ArrayList<>(Arrays.asList("Monsieur", "unfurnished", "swoon", "compassion", "astray", "prejudice", "abominable", "elegance", "wailful", "squabble", "character", "dismay", "exploitation", "synonymous", "interfere", "idealistic", "engineering", "international", "hygienic", "persuasion", "humiliation", "campaign", "background", "neighbourhood", "cutlass", "twitch", "pirates", "magistrate", "dominant", "soar", "phenomenal", "essentially", "Gutenberg", "barricade", "influence", "antenna", "infiltration", "virtual", "unprecedented", "formidable", "shriek", "reverie", "pronunciation", "brochure", "spectacle", "inquisition", "holphither", "verdure", "inveterate", "fortitude", "authoritarian", "coercion", "franchise", "shortcoming", "circumstances", "envision", "prosecution", "accentuate", "stringent"));
        row2 = new ArrayList<>(Arrays.asList("confinement", "seclusion", "haggard",  "abstraction", "pedestrian", "trifling",  "accomplish",  "conspiracy",  "Afghanistan",  "corduroy",  "appreciation",  "sabres", "argumentative",  "indelible",  "prevalent",  "discrimination",  "servitude",  "untouchability", "partake",  "hypocrisy",  "excommunication",  "unanimous",  "maintenance",  "downstairs",  "deformation", "sober",  "deposition",  "extraordinary",  "ponder",  "gravitational",  "accommodate",  "intellectual", "ancestors",  "standalone",  "demonstration",  "beginning",  "revolutionary",  "excerpt",  "achievement", "creatures",  "tantalizing",  "approach",  "articulate",  "transformation",  "wreck",  "remembrance", "perfidious",  "prerogative", "treacherous", "constitution", "periodically", "amendment", "approval", "marginalise", "innumerable", "designation", "hierarchy", "ghettoisation", "dispossess"));
        row3 = new ArrayList<>(Arrays.asList("solitude",  "vagrancy",  "steadfastly",  "unconsciousness",  "carriage", "conversation",  "countenance",  "rhetorical",  "mantle",  "redemption",  "autobiography",  "Cossack", "exceptional",  "enslavement",  "negotiable",  "inequality",  "instrumental",  "extensive",  "environment", "disappointment",  "community",  "bureaucrats",  "equipments",  "stroke",  "gracious",  "nimbleness", "apoplexy",  "universe",  "levitate",  "allusion",  "advertisement",  "pursuit",  "innovation", "cumbersome",  "photojournalism",  "swathe",  "interactive",  "impressionable",  "compartment", "landscape",  "confusion",  "remarkable",  "throughout",  "ignorant",  "provision",  "warrant", "signories",  "confederates",  "carcass",  "tyranny",  "separation",  "establishment",  "representative", "constituency",  "sedition",  "cognizable",  "marginalization",  "commitment",  "reprehensible"));
        row4 = new ArrayList<String>(Arrays.asList("garret", "endeavour", "obliterate", "spectator", "intermission", "scamper", "substantial", "ciderpress", "eerie", "manoeuvre", "league", "repetition", "distinguish", "deprivation", "herculean", "acquaintance", "centenary", "organisation", "skeptical", "writ", "intervention", "rehabilitation", "scarcely", "particularly", "wrench", "parlour", "treasure", "invisible", "meteor", "gregarious", "celluloid", "precursor", "widespread", "portrait", "audience", "disseminate", "convenience", "confidential", "anxious", "pleasantly", "apology", "suspense", "sturdy", "mantle", "vessel", "abysm", "government", "ignoble", "cherubim", "extraordinary", "retaliation", "mechanism", "coalition", "controversial", "acquittal", "trial", "malnourished", "ostracise", "scavenger"));
        row5 = new ArrayList<>(Arrays.asList("rehabilitation",  "settlement",  "economically",  "determinants",  "afforestation",  "susceptible",  "decomposers",  "depletion",  "sedimentary",  "conventional",  "manure",  "subsistence",  "ailments",  "artificially",  "intolerance",  "census",  "flourish",  "amass",  "hostage",  "doctrine",  "professional",  "conquest",  "oppressive",  "struggle",  "traditional",  "committee",  "assortment",  "anglicise",  "subtleties",  "capacity",  "reformation",  "opportunity",  "scripture",  "perception",  "triumph",  "mythological",  "Styrofoam",  "diffusion",  "accommodation",  "celestial",  "satellite",  "asterism",  "infrastructure",  "geyser",  "ozonisation",  "vulnerable",  "precipitation",  "fungicide",  "leguminous",  "erosion",  "fumigate",  "temporary",  "cellulose",  "chromosome",  "algae",  "botulism",  "pasteurisation",  "polymerisation",  "conductivity",  "gammexane ",  "extinguish",  "electrostatic"));
        row6 = new ArrayList<>(Arrays.asList("access",  "procedure", "replenish",  "erosion",  "terrace",  "scarcity",  "deciduous",  "ethical",  "igneous",  "hydroelectric",  "approximate",  "regeneration",  "machinery",  "immigrants",  "superstitious",  "reluctant",  "sovereignty",  "alliance",  "injunction",  "expansion",  "musket",  "contemporary",  "subsequently",  "miraculous",  "embankment",  "conquer",  "industrialization",  "concede",  "vernacular",  "missionary",  "suffrage",  "subordinate",  "sanctification",  "invariably",  "invincible",  "frequency",  "echocardiography",  "electroscope",  "refraction",  "hypermetropia",  "gravitational",  "asteroid",  "seismograph",  "renewable",  "bituminous",  "chlorination",  "sanctuary",  "configuration",  "transplantation",  "eutrophication",  "sprinkler",  "albumen",  "nucleus",  "permeable",  "dicotyledonous",  "plankton",  "mycelium",  "adolescence",  "hydrophobia",  "malleable",  "disinfectant", "temperature",  "gravitational"));
        row7 = new ArrayList<>(Arrays.asList("sustainable",  "beneficiary",  "ubiquitous",  "desertification",  "primitive",  "pesticide",  "vulnerable",  "solubility",  "antimony",  "inexhaustible",  "arable",  "adequate",  "technique",  "curious",  "enlightenment",  "rebellion",  "mercantile",  "subsidiary",  "territory",  "impeachment",  "colonial",  "commission",  "optimistic",  "kinship",  "initiative",  "exquisite",  "metallurgy",  "orientalism",  "attempt",  "persuade",  "municipality",  "propagate",  "condemn",  "governance",  "perspective",  "percussion",  "ultrasonography",  "insulator",  "kaleidoscope",  "nutrition",  "constellation",  "meteoroid",  "catastrophic",  "commercial",  "haemoglobin",  "biodiversity",  "hybridization",  "accumulation",  "organism ",  "chloroplast",  "membrane",  "epithelial",  "lactobacilli",  "pseudopodia",  "synthetic",  "lubricant",  "decomposition",  "combustion",  "vaporization",  "resistance"));
        row8 = new ArrayList<>(Arrays.asList("tragedy",  "hazardous",  "uninhabitable",  "scientific",  "precipitation", "effluents",  "endangered",  "quarrying",  "sufficient",  "immemorial",  "pisciculture",  "ownership", "availability",  "religious",  "subjugation",  "concession",  "appearance",  "subservience",  "paramount", "collectorate",  "assumption",  "revenue",  "pessimistic",  "fallow",  "legacy",  "enormous",  "embedded", "linguist",  "surrounding",  "association",  "palanquin",  "indigenous",  "orthodox",  "imperious", "miniature",  "larynx",  "attraction",  "electrolysis",  "cataract",  "universe",  "terrestrial",  "elongate", "disastrous",  "geothermal",  "ultraviolet",  "convention",  "drought",  "viscosity",  "symbiosis",  "inundation", "winnowing",  "unicellular",  "rigid",  "vascular",  "organelle",  "aerobic",  "perishable", "plasticity",  "distillation", "tincture",  "ignition",  "pressure",  "amplitude"));

        words = new ArrayList<>(Arrays.asList(row1, row2, row3, row4, row5, row6, row7, row8));

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        rows = new ArrayList<>();
        rows.add("Row 1");
        rows.add("Row 2");
        rows.add("Row 3");
        rows.add("Row 4");
        rows.add("Row 5");
        rows.add("Row 6");
        rows.add("Row 7");
        rows.add("Row 8");

        rowAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.select_dialog_item, rows);
        rowView.setAdapter(rowAdapter);
        rowView.setThreshold(1);

        rowView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                rowView.setError(null);
                rowNo = i;
            }
        });

        micView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        spokenWordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    spokenWord = spokenWordView.getText().toString();
                    spokenWord = spokenWord.replace(" ", "");
                    if (currIndex < 0 || currIndex >= words.get(rowNo).size()) {
                        Toast.makeText(MainActivity.this, "Press Next first.", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (words.get(rowNo).get(currIndex).equals(spokenWord)) {
                        tts.speak("RIGHT", TextToSpeech.QUEUE_FLUSH, null);
                        spokenWordLayout.setBoxBackgroundColor(getResources().getColor(R.color.green));
                    }
                    else {
                        tts.speak("WRONG. It's not   "+spokenWord, TextToSpeech.QUEUE_FLUSH, null);
                        spokenWordLayout.setBoxBackgroundColor(getResources().getColor(R.color.red));
                    }
                }
                return true;
            }
        });

        spokenWordLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spokenWord = spokenWordView.getText().toString();
                spokenWord = spokenWord.replace(" ", "");

                if (currIndex < 0 || currIndex >= words.get(rowNo).size()) {
                    Toast.makeText(MainActivity.this, "Press Next first.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (words.get(rowNo).get(currIndex).equals(spokenWord)) {
                    tts.speak("RIGHT", TextToSpeech.QUEUE_FLUSH, null);
                    spokenWordLayout.setBoxBackgroundColor(getResources().getColor(R.color.green));
                }
                else {
                    tts.speak("WRONG. It's not   "+spokenWord, TextToSpeech.QUEUE_FLUSH, null);
                    spokenWordLayout.setBoxBackgroundColor(getResources().getColor(R.color.red));
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spokenWordView.setText("");
                spokenWordLayout.setBoxBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currIndex--;
                if (currIndex < 0)
                    currIndex = words.get(rowNo).size() - 1;
                tts.speak(words.get(rowNo).get(currIndex), TextToSpeech.QUEUE_FLUSH, null);
                spokenWordView.setText("");
                spokenWordLayout.setBoxBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        giveUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currIndex < 0 || currIndex >= words.get(rowNo).size()) {
                    Toast.makeText(MainActivity.this, "Press Next first.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Snackbar.make(constraintLayout, "Answer: " + words.get(rowNo).get(currIndex), BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currIndex++;
                if (currIndex >= words.get(rowNo).size())
                    currIndex = 0;
                tts.speak(words.get(rowNo).get(currIndex), TextToSpeech.QUEUE_FLUSH, null);
                spokenWordView.setText("");
                spokenWordLayout.setBoxBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currIndex < 0 || currIndex >= words.get(rowNo).size()) {
                    Toast.makeText(MainActivity.this, "Press Next first.", Toast.LENGTH_SHORT).show();
                    return;
                }
                tts.speak(words.get(rowNo).get(currIndex), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                spokenWordView.setText(Objects.requireNonNull(result).get(0));
            }
        }
    }
}