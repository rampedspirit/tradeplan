module.exports = {
    module: {
        rules: [
            {
                test: /\.pegjs$/,
                loader: 'pegjs-loader'
            }
        ]
    }
};